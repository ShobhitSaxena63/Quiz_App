package com.shobhit63.quizo.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shobhit63.quizo.data.category.TopicRepository
import com.shobhit63.quizo.data.category.Topics

class TopicViewModel(application: Application):AndroidViewModel(application) {
    private val repo:TopicRepository = TopicRepository(application)

    val topicList:LiveData<List<Topics>> = repo.getTopicR()
}