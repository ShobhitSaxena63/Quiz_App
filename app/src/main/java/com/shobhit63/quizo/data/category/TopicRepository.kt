package com.shobhit63.quizo.data.category

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData

class TopicRepository(context: Application) {
    private val topicsDao:TopicsDao = TopicDatabase.getDatabase(context).topicDao()

    fun getTopicR():LiveData<List<Topics>> {
        return topicsDao.getTopics()
    }
}