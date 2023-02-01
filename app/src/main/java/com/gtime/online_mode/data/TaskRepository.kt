package com.gtime.online_mode.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.gtime.general.Constants
import com.gtime.general.scopes.AppScope
import com.gtime.online_mode.data.model.TaskModel
import com.gtime.online_mode.state_classes.StateOfRequests
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@AppScope
class TaskRepository @Inject constructor(
    @Named(Constants.TASK_STORAGE_COLLECTION) private val taskReference: CollectionReference,
    private val auth: FirebaseAuth
) {
    val tasks = MutableLiveData<List<TaskModel>>()
    val state = MutableLiveData<StateOfRequests>()

    suspend fun getTasks() = withContext(Dispatchers.IO) {
        val email = auth.currentUser?.email
        if (email == null) {
            state.postValue(StateOfRequests.Error.AuthError)
            return@withContext
        }
        taskReference.document(email).collection(Constants.TASK_USER_COLLECTION).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val taskTemp = mutableListOf<TaskModel>()
                    task.result.forEach { document ->
                        val obj = document.toObject(TaskModel::class.java)
                        taskTemp.add(
                            TaskModel(
                                id = document.id,
                                award = obj.award,
                                description = obj.description,
                                state = obj.state
                            )
                        )
                    }
                    tasks.postValue(taskTemp)
                    state.postValue(StateOfRequests.Success.FullSuccess)
                } else {
                    state.postValue(StateOfRequests.Error.Failure)
                }
            }
    }

    suspend fun changeState(id: String, state: String) {
        val email = auth.currentUser?.email ?: return
        taskReference.document(email).collection(Constants.TASK_USER_COLLECTION).document(id).set(
            hashMapOf(Constants.STATE_FIELD to state), SetOptions.merge()
        )
        getTasks()
    }

}