package com.shobhit63.quizo.data.questions

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shobhit63.quizo.data.category.Topics
import com.shobhit63.quizo.data.category.TopicsDao


@Database(entities = [Questions::class],version=1)
abstract class QuestionsDatabase: RoomDatabase() {
    abstract fun questionsDao(): QuestionsDao

    companion object{
        @Volatile
        private var instance:QuestionsDatabase?=null

        fun getDatabase(context: Context) =instance
            ?: synchronized(this){
                Room.databaseBuilder(context.applicationContext,QuestionsDatabase::class.java,"questions_table")
                    .createFromAsset("database/questions.db")
                    .build().also { instance =  it }
            }
    }
}