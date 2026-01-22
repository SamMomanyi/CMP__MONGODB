package org.example.project.data.database

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.example.project.core.domain.RequestState

class MongoDB  {
    var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm(){
        if(realm == null || realm!!.isClosed()){
            val config  = RealmConfiguration.Builder(
                schema = setOf(ToDoTask::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun readActiveTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>( "completed == $0",false)
            ?.asFlow()
            ?.map {result ->
                RequestState.Success(
                    data = result.list.sortedByDescending {task -> task.favorite}
                )
            } ?: flow {RequestState.Error(message = "Realm is not available")}
    }

    fun readCompletedTasks(): Flow<RequestState<List<ToDoTask>>> {
        return realm?.query<ToDoTask>( "completed == $0",true)
            ?.asFlow()
            ?.map {result ->
                RequestState.Success(
                    data = result.list.sortedByDescending {task -> task.favorite}
                )
            } ?: flow {RequestState.Error(message = "Realm is not available")}
    }

   suspend  fun addTask(task :  ToDoTask){
        realm?.write {copyToRealm(task)}
    }

    //a couple of more functions
    //WE search for latest version of an object task(by passing it's id) and update it's properties
    suspend fun updateTask(task : ToDoTask){
        realm?.write{
            try{
                val queriedTask = query<ToDoTask>("_id == $0", task._id)
                    .first()
                    .find()
                queriedTask?.let{
                    findLatest(it)?.let { currentTask->
                        currentTask.title = task.title
                        currentTask.description = task.description
                    }
                }
            } catch (e: Exception){
                println(e)
            }
        }
    }

    //here we are updating the completed property
    suspend fun setCompleted(task: ToDoTask, taskCompleted : Boolean){
        realm?.write {
            try{
                val queriedTask = query<ToDoTask>(query = "_id == $0", task._id)
                    .find()
                    .first()
                queriedTask.apply { completed =  taskCompleted }
            } catch (e :  Exception){
                println(e)
            }
        }
    }

    //we pass a boolean object to the task , then query and update it's id , we normally just pass the id instead of the whole object
    suspend fun setFavorite(task: ToDoTask, isFavorite: Boolean){
        realm?.write{
            try{
                val queriedTask = query<ToDoTask>(query = "_id == $0", task._id)
                    .find()
                    .first()
                queriedTask.apply { favorite = isFavorite}
            }catch (e : Exception){
                println(e)
            }
        }
    }

    //one more function to delete a selected task
    //we can later on use the RequestState class to return an error if an operation fails instead of merely printing the error
    suspend fun deleteTask(task : ToDoTask){
        realm?.write {
            try{
                //query fro the task using a query function
                val queriedTask = query<ToDoTask>(query = "_id == $0", task._id)
                    .first()
                    .find()
                queriedTask?.let{
                    findLatest(it)?.let { currentTask ->
                        delete(currentTask)
                    }
                }
            } catch (e : Exception){
                println(e)
            }
        }
    }
}