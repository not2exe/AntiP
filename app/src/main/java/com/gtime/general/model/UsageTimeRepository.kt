package com.gtime.general.model

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.gtime.general.Cache
import com.gtime.general.Constants
import com.gtime.general.KindOfApps
import com.gtime.general.model.dataclasses.AppEntity
import com.gtime.general.model.db.AppDataBaseEntity
import com.gtime.general.model.db.AppTableDao
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.OnlineModeDivisionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt


@AppScope
class UsageTimeRepository @Inject constructor(
    private val appDao: AppTableDao,
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val scope: CoroutineScope,
    private val cache: Cache,
    private val onlineModeDivisionRepository: OnlineModeDivisionRepository
) {
    val usefulApps = MutableLiveData<List<AppEntity>>()
    val toxicApps = MutableLiveData<List<AppEntity>>()
    val neutralApps = MutableLiveData<List<AppEntity>>()
    val uiGeneralScores = MutableLiveData<Int>()

    private val isSwapEnded = AtomicBoolean(true)

    init {
        scope.launch {
            refreshAll()
            fromDataBaseToRep()
            refreshUsageApps()
        }
    }


    suspend fun setMultiplier(appDataBaseEntity: AppDataBaseEntity) =
        withContext(Dispatchers.IO) {
            when (appDataBaseEntity.kindOfApp) {
                KindOfApps.TOXIC -> {
                    var list = getMutableList(toxicApps)
                    list = findAndReplace(list, appDataBaseEntity)
                        ?: return@withContext
                    toxicApps.postValue(list)
                }
                KindOfApps.USEFUL -> {
                    var list = getMutableList(usefulApps)
                    list = findAndReplace(list, appDataBaseEntity)
                        ?: return@withContext
                    usefulApps.postValue(list)
                }
                KindOfApps.NEUTRAL -> {
                    var list = getMutableList(neutralApps)
                    list = findAndReplace(list, appDataBaseEntity)
                        ?: return@withContext
                    neutralApps.postValue(list)
                }
            }
            appDao.insert(appDataBaseEntity)
            refreshOnlyScores()
        }

    private fun findAndReplace(
        list: MutableList<AppEntity>,
        appDataBaseEntity: AppDataBaseEntity,
    ): MutableList<AppEntity>? {
        val appEnt = list.find { it.packageName == appDataBaseEntity.packageName }
            ?: return null
        if (appEnt.multiplier == appDataBaseEntity.multiplier) return null
        list.remove(appEnt)
        list.add(
            AppEntity(
                image = appEnt.image,
                packageName = appEnt.packageName,
                name = appEnt.name,
                kindOfApps = appEnt.kindOfApps,
                multiplier = appDataBaseEntity.multiplier,
                _scores = appEnt.getScoresWithoutMultiplier(),
                isGame = appEnt.isGame,
                percentsOsGeneral = appEnt.percentsOsGeneral
            )
        )
        return list
    }

    suspend fun put(appEntity: AppEntity, kindOfApps: KindOfApps) {
        val packageName = appEntity.packageName ?: ""
        val multiplier: Double
        when (kindOfApps) {
            KindOfApps.USEFUL -> {
                multiplier = abs(appEntity.multiplier)
                putIntoUseful(appEntity, multiplier)
            }
            KindOfApps.TOXIC -> {
                multiplier = abs(appEntity.multiplier) * -1
                putIntoHarmful(appEntity, multiplier)
            }
            KindOfApps.NEUTRAL -> {
                multiplier = appEntity.multiplier
                putIntoOthers(appEntity, multiplier)
            }
        }
        appDao.insert(AppDataBaseEntity(packageName, appEntity.isGame, kindOfApps, multiplier))
        refreshOnlyScores()
    }


    suspend fun refreshUsageApps() = withContext(Dispatchers.IO) {
        val useful = getMutableList(usefulApps)
        val harmful = getMutableList(toxicApps)
        val neutral = getMutableList(neutralApps)
        if (!isSwapEnded.get()) {
            toxicApps.postValue(harmful)
            usefulApps.postValue(useful)
            neutralApps.postValue(neutral)
            return@withContext
        }
        var uiScores = 0
        val start: Calendar = (Calendar.getInstance())
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.MILLISECOND, 0)
        val end: Calendar = Calendar.getInstance()
        val mapOfTime: Map<String, Long> =
            getAppsInfo(start.timeInMillis, end.timeInMillis)
        val scoresOfAll = mapOfTime[Constants.TIME_OF_ALL] ?: 0

        mapOfTime.keys.forEach { packageName ->
            val time = mapOfTime[packageName] ?: 0
            val scores = toScore(time)
            val percents: Int = ((time.toDouble() / scoresOfAll.toDouble()) * 100).roundToInt()
            var elemInList = removeAndReturn(useful, packageName)
            if (elemInList != null) {
                handleAdd(useful, percents, scores, elemInList)
                uiScores += (scores * elemInList.multiplier).roundToInt()
            }
            elemInList = removeAndReturn(harmful, packageName)
            if (elemInList != null) {
                handleAdd(harmful, percents, scores, elemInList)
                uiScores += (scores * elemInList.multiplier).roundToInt()
            }
            elemInList = removeAndReturn(neutral, packageName)
            if (elemInList != null) {
                handleAdd(neutral, percents, scores, elemInList)
            }
        }
        uiGeneralScores.postValue(uiScores)
        toxicApps.postValue(harmful)
        usefulApps.postValue(useful)
        neutralApps.postValue(neutral)
    }


    suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val listOfAllOnDevice =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (i in listOfAllOnDevice.indices) {
            if (!appDao.isAppInDatabase(listOfAllOnDevice[i].packageName) &&
                packageManager.getLaunchIntentForPackage(listOfAllOnDevice[i].packageName) != null
                && (listOfAllOnDevice[i].flags and ApplicationInfo.FLAG_SYSTEM) == 0
                && (listOfAllOnDevice[i].flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0
            ) {
                appDao.insert(
                    AppDataBaseEntity(
                        packageName = listOfAllOnDevice[i].packageName,
                        isGame = listOfAllOnDevice[i].category == ApplicationInfo.CATEGORY_GAME
                    )
                )
            }
        }
    }

    fun swap() = scope.launch {
        isSwapEnded.set(false)
        val neutral = getMutableList(neutralApps)
        val harmful = getMutableList(toxicApps)
        val useful = getMutableList(usefulApps)
        val list = mutableListOf<AppEntity>()
        neutral.forEach { list.add(it) }
        useful.forEach { list.add(it) }
        harmful.forEach { list.add(it) }
        neutral.clear()
        harmful.clear()
        useful.clear()
        if (cache.isOnline()) {
            swapFromOfflineToOnline(
                mergedList = list,
                neutral = neutral,
                harmful = harmful,
                useful = useful
            )
        } else {
            swapFromOnlineToOffline(
                mergedList = list,
                neutral = neutral,
                harmful = harmful,
                useful = useful
            )
        }
        neutralApps.postValue(neutral)
        toxicApps.postValue(harmful)
        usefulApps.postValue(useful)
        refreshOnlyScores()
        isSwapEnded.set(true)
    }

    suspend fun fromDataBaseToRep() = withContext(Dispatchers.IO) {
        val list = appDao.getAll().map { toAppEntity(it) }
        val neutral = mutableListOf<AppEntity>()
        val useful = mutableListOf<AppEntity>()
        val harmful = mutableListOf<AppEntity>()
        if (cache.isOnline()) {
            swapFromOfflineToOnline(
                mergedList = list,
                neutral = neutral,
                harmful = harmful,
                useful = useful
            )
        } else {
            swapFromOnlineToOffline(
                mergedList = list,
                neutral = neutral,
                harmful = harmful,
                useful = useful
            )
        }
        neutralApps.postValue(neutral)
        usefulApps.postValue(useful)
        toxicApps.postValue(harmful)
    }

    private fun toAppEntity(ent: AppDataBaseEntity): AppEntity = AppEntity(
        image = getIconApp(ent.packageName),
        packageName = ent.packageName,
        name = getName(ent.packageName),
        kindOfApps = ent.kindOfApp,
        isGame = ent.isGame
    )

    private suspend fun swapFromOfflineToOnline(
        mergedList: List<AppEntity>,
        neutral: MutableList<AppEntity>,
        harmful: MutableList<AppEntity>,
        useful: MutableList<AppEntity>
    ) = withContext(Dispatchers.IO) {
        val onlineModeDivisionModel = onlineModeDivisionRepository.get()
        val harmfulPackageNames = onlineModeDivisionModel.toxic.toSet()
        val usefulPackageNames = onlineModeDivisionModel.useful.toSet()
        for (i in mergedList.indices) {
            val icon = getIconApp(mergedList[i].packageName ?: "")
            val name = getName(mergedList[i].packageName ?: "")
            val scores = mergedList[i].getScoresWithoutMultiplier()
            when (mergedList[i].packageName) {
                in harmfulPackageNames -> {
                    harmful.add(
                        AppEntity(
                            image = icon,
                            packageName = mergedList[i].packageName,
                            name = name,
                            kindOfApps = KindOfApps.TOXIC,
                            isGame = mergedList[i].isGame,
                            _scores = scores,
                            percentsOsGeneral = mergedList[i].percentsOsGeneral,
                            multiplier = -1.0,
                        )
                    )
                }
                in usefulPackageNames -> {
                    useful.add(
                        AppEntity(
                            image = icon,
                            packageName = mergedList[i].packageName,
                            name = name,
                            kindOfApps = KindOfApps.USEFUL,
                            isGame = mergedList[i].isGame,
                            multiplier = 1.0,
                            percentsOsGeneral = mergedList[i].percentsOsGeneral,
                            _scores = scores
                        )
                    )
                }
                else -> {
                    if (mergedList[i].isGame) {
                        harmful.add(
                            AppEntity(
                                image = icon,
                                packageName = mergedList[i].packageName,
                                name = name,
                                kindOfApps = KindOfApps.TOXIC,
                                isGame = mergedList[i].isGame,
                                _scores = scores,
                                percentsOsGeneral = mergedList[i].percentsOsGeneral,
                                multiplier = -1.0,
                            )
                        )
                    } else {
                        neutral.add(
                            AppEntity(
                                image = icon,
                                packageName = mergedList[i].packageName,
                                name = name,
                                kindOfApps = KindOfApps.NEUTRAL,
                                isGame = mergedList[i].isGame,
                                multiplier = 1.0,
                                percentsOsGeneral = mergedList[i].percentsOsGeneral,
                                _scores = scores
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun swapFromOnlineToOffline(
        mergedList: List<AppEntity>,
        neutral: MutableList<AppEntity>,
        harmful: MutableList<AppEntity>,
        useful: MutableList<AppEntity>
    ) = withContext(Dispatchers.IO)
    {
        for (i in mergedList.indices) {
            val packageName = mergedList[i].packageName ?: ""
            val elemFromDb = appDao.getByName(packageName)
            val icon = getIconApp(packageName)
            val name = getName(packageName)
            val appEnt = AppEntity(
                icon,
                mergedList[i].packageName,
                name,
                elemFromDb.kindOfApp,
                isGame = mergedList[i].isGame,
                multiplier = elemFromDb.multiplier,
                percentsOsGeneral = mergedList[i].percentsOsGeneral,
            )
            when (elemFromDb.kindOfApp) {
                KindOfApps.TOXIC -> {
                    harmful.add(
                        appEnt
                    )
                }
                KindOfApps.USEFUL -> {
                    useful.add(
                        appEnt
                    )
                }
                KindOfApps.NEUTRAL -> {
                    neutral.add(
                        appEnt
                    )
                }
            }
        }
    }

    private fun getAppsInfo(
        startTime: Long,
        endTime: Long
    ): HashMap<String, Long> {
        val allEvents: ArrayList<UsageEvents.Event> =
            getFilteredEvents(usageStatsManager.queryEvents(startTime, endTime))
        val map: HashMap<String, Long> = HashMap()

        for (i in 0 until allEvents.size - 1) {
            val event0 = allEvents[i]
            val event1 = allEvents[i + 1]
            if (event0.eventType == UsageEvents.Event.ACTIVITY_RESUMED &&
                (event1.eventType == UsageEvents.Event.ACTIVITY_PAUSED || event1.eventType == UsageEvents.Event.ACTIVITY_STOPPED)
                && event0.packageName == event1.packageName
            ) {
                val timeBetweenActions = (event1.timeStamp - event0.timeStamp)
                map[event0.packageName] =
                    (map[event0.packageName] ?: 0L) + timeBetweenActions
                map[Constants.TIME_OF_ALL] = (map[Constants.TIME_OF_ALL] ?: 0L) + timeBetweenActions
            }
        }
        return map
    }

    private fun getFilteredEvents(usageEvents: UsageEvents): ArrayList<UsageEvents.Event> {
        val allEvents = ArrayList<UsageEvents.Event>()
        var currentEvent: UsageEvents.Event
        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
                || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED
            ) {
                allEvents.add(currentEvent)
            }
        }
        return allEvents
    }

    private fun getIconApp(packageName: String): Drawable? = try {
        packageManager.getApplicationIcon(packageName)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }

    private fun getName(packageName: String): String = try {
        packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
        ).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

    private fun handleAdd(
        list: MutableList<AppEntity>,
        percents: Int,
        scores: Int,
        elem: AppEntity
    ) {
        list.add(
            AppEntity(
                name = elem.name,
                packageName = elem.packageName,
                image = elem.image,
                percentsOsGeneral = percents,
                _scores = scores,
                kindOfApps = elem.kindOfApps,
                multiplier = elem.multiplier,
                isGame = elem.isGame,
            )
        )
    }

    private fun removeAndReturn(list: MutableList<AppEntity>, packageName: String): AppEntity? {
        var entity: AppEntity? = null
        for (i in 0 until list.size) {
            if (list[i].packageName == packageName) {
                entity = list[i]
                list.removeAt(i)
                break
            }
        }
        return entity
    }

    private suspend fun getMutableList(liveData: MutableLiveData<List<AppEntity>>): MutableList<AppEntity> =
        withContext(Dispatchers.Main) {
            (liveData.value ?: emptyList()).toMutableList()
        }

    private fun toScore(scores: Long): Int = (scores / 10000).toInt()

    private suspend fun putIntoHarmful(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(toxicApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.getScoresWithoutMultiplier(),
                    multiplier = multiplier,
                    isGame = appEntity.isGame
                )
            )
            toxicApps.postValue(list)
        }

    private suspend fun putIntoUseful(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(usefulApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.getScoresWithoutMultiplier(),
                    multiplier = multiplier,
                    isGame = appEntity.isGame
                )
            )
            usefulApps.postValue(list)
        }

    private suspend fun putIntoOthers(appEntity: AppEntity, multiplier: Double) =
        withContext(Dispatchers.IO) {
            val list = getMutableList(neutralApps)
            list.add(
                AppEntity(
                    image = appEntity.image,
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    kindOfApps = appEntity.kindOfApps,
                    percentsOsGeneral = appEntity.percentsOsGeneral,
                    _scores = appEntity.getScoresWithoutMultiplier(),
                    multiplier = multiplier,
                    isGame = appEntity.isGame
                )
            )
            neutralApps.postValue(list)
        }

    private suspend fun refreshOnlyScores() = withContext(Dispatchers.Main) {
        var scores = 0
        toxicApps.value?.forEach {
            scores += it.scores
        }
        usefulApps.value?.forEach {
            scores += it.scores
        }
        uiGeneralScores.value = scores
    }

    suspend fun removeFromHarmful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(toxicApps).filter { it != appEntity }
        toxicApps.postValue(list)
    }

    suspend fun removeFromUseful(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(usefulApps).filter { it != appEntity }
        usefulApps.postValue(list)
    }

    suspend fun removeFromOthers(appEntity: AppEntity) = withContext(Dispatchers.IO) {
        val list = getMutableList(neutralApps).filter { it != appEntity }
        neutralApps.postValue(list)
    }

    suspend fun isNotInit(): Boolean = withContext(Dispatchers.Main) {
        (toxicApps.value == null && neutralApps.value == null && usefulApps.value == null)
    }
}



