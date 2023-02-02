package com.gtime.online_mode.data

import com.google.firebase.firestore.CollectionReference
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.OnlineModeDivisionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class OnlineModeDivisionRepository @Inject constructor(@Named(Constants.PREDIVISION_OF_APPS_COLLECTION) private val preDivisionOfAppsReference: CollectionReference) {
    suspend fun get(): OnlineModeDivisionModel = withContext(Dispatchers.IO) {
        return@withContext preDivisionOfAppsReference.get().await()
            .toObjects(OnlineModeDivisionModel::class.java).firstOrNull()
            ?: OnlineModeDivisionModel()
    }
}