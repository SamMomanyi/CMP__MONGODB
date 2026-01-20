package org.example.project.domain


// 1. IMPORTANT: These are the required imports
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ToDoTask : RealmObject {
    // 2. Use @PrimaryKey and ObjectId (not ObjectID)
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var title: String = ""
    var description: String = "" // Fixed typo 'decription'
    var favorite: Boolean = false
    var completed: Boolean = false
}