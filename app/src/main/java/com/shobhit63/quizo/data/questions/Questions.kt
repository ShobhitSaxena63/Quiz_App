package com.shobhit63.quizo.data.questions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Questions(@PrimaryKey(autoGenerate = true) val id:Int, val category:String, val question:String, val option_one:String, val option_two:String, val option_three:String,
                     val option_four:String, val correct_answer:String)