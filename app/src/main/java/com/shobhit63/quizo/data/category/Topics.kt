package com.shobhit63.quizo.data.category

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category")
data class Topics(@PrimaryKey(autoGenerate = true) val id:Int,
                  val topic:String?)
