package org.example.project.presentation.screen.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.example.project.data.database.MongoDB
import org.example.project.data.database.ToDoTask
import org.example.project.domain.TaskAction

//we will inject  a mongoDB instance and add two functions for adding and deleting task
//we then need to add this inside the module so it's specified how to get an instance of Koin library
class TaskViewModel(
    private val mongoDB: MongoDB
) : ScreenModel {

    fun setAction(action : TaskAction){
        when(action){
            is TaskAction.Add -> {
                addTask(action.task)
            }
            is TaskAction.Update -> {
                updateTask(action.task)
            }
            else -> {

            }
        }
    }

    //this functions are private since we will not be exposing them to the UI
    private fun addTask(task : ToDoTask){
        screenModelScope.launch(Dispatchers.IO){
            mongoDB.addTask(task)
        }
    }

    private fun updateTask(task :  ToDoTask){
        screenModelScope.launch(Dispatchers.IO){
            mongoDB.updateTask(task)
        }
    }
}