package com.shobhit63.quizo.data.questions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.shobhit63.quizo.data.category.Topics

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM questions WHERE category =:category ORDER BY Random() LIMIT 10")
    fun getQuestions(category:String): LiveData<List<Questions>>

}