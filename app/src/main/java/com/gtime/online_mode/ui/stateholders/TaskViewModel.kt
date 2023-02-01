package com.gtime.online_mode.ui.stateholders

import androidx.lifecycle.*
import com.gtime.general.Constants
import com.gtime.online_mode.data.CoinsRepository
import com.gtime.online_mode.data.TaskRepository
import com.gtime.online_mode.data.model.TaskModel
import com.gtime.online_mode.ui.StateOfTask
import com.gtime.online_mode.ui.TaskUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val coinsRepository: CoinsRepository
) :
    ViewModel() {
    init {
        viewModelScope.launch {
            taskRepository.getTasks()
        }
    }

    val tasks = Transformations.switchMap(taskRepository.tasks) { convertToUI(it) }
    val state = Transformations.switchMap(taskRepository.state) { MutableLiveData(it) }
    fun onClickClaim(id: String, stateOfTask: StateOfTask, award: Int) = viewModelScope.launch {
        taskRepository.changeState(id, convertFromStateToString(stateOfTask))
        coinsRepository.addCoins(award)
    }

    private fun convertFromStateToString(stateOfTask: StateOfTask): String = when (stateOfTask) {
        StateOfTask.CLAIMED -> Constants.TASK_STATE_CLAIMED
        StateOfTask.READY_TO_CLAIM -> Constants.TASK_STATE_READY_TO_CLAIM
        StateOfTask.UNCOMPLETED -> Constants.TASK_STATE_UNCOMPLETED
    }

    private fun convertFromStringToState(state: String): StateOfTask? = when (state) {
        Constants.TASK_STATE_CLAIMED -> StateOfTask.CLAIMED
        Constants.TASK_STATE_READY_TO_CLAIM -> StateOfTask.READY_TO_CLAIM
        Constants.TASK_STATE_UNCOMPLETED -> StateOfTask.UNCOMPLETED
        else -> null
    }

    private fun toUiObject(taskModel: TaskModel): TaskUiModel =
        TaskUiModel(
            id = taskModel.id,
            award = taskModel.award,
            state = convertFromStringToState(taskModel.state),
            description = taskModel.description
        )

    private fun convertToUI(list: List<TaskModel>): MutableLiveData<List<TaskUiModel>> =
        MutableLiveData(list.map { toUiObject(it) })

    fun refresh() = viewModelScope.launch {
        taskRepository.getTasks()
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): TaskViewModel
    }
}
