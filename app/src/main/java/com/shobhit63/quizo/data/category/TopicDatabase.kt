package com.shobhit63.quizo.data.category

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Topics::class],version=1)
abstract class TopicDatabase:RoomDatabase() {
    abstract fun topicDao():TopicsDao

    companion object{
        @Volatile
        private var instance:TopicDatabase?=null

        fun getDatabase(context: Context) =instance
            ?: synchronized(this){
                Room.databaseBuilder(context.applicationContext,TopicDatabase::class.java,"category_table")
                    .createFromAsset("database/category.db")
                    .build().also { instance =  it }
            }
    }
}