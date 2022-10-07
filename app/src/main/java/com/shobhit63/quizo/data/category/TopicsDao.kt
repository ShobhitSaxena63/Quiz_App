package com.shobhit63.quizo.data.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TopicsDao {
    @Query("SELECT * FROM category")
    fun getTopics():LiveData<List<Topics>>
}