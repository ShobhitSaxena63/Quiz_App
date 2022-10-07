package com.shobhit63.quizo.data.questions

import android.app.Application
import androidx.lifecycle.LiveData
import com.shobhit63.quizo.data.category.TopicDatabase
import com.shobhit63.quizo.data.category.Topics
import com.shobhit63.quizo.data.category.TopicsDao


class QuestionsRepository(context: Application) {
    private val questionsDao: QuestionsDao = QuestionsDatabase.getDatabase(context).questionsDao()

    fun getQuestion(category:String): LiveData<List<Questions>> {
        return questionsDao.getQuestions(category)
    }
}