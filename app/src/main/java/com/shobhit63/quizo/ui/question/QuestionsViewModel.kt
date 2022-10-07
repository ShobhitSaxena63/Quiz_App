package com.shobhit63.quizo.ui.question

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shobhit63.quizo.data.questions.Questions
import com.shobhit63.quizo.data.questions.QuestionsRepository


class QuestionsViewModel(application: Application): AndroidViewModel(application) {
    private val repo: QuestionsRepository = QuestionsRepository(application)


    private val _category = MutableLiveData<String>()
    val category:LiveData<String>
        get() = _category
    val questionList:LiveData<List<Questions>> = Transformations.switchMap(_category){
        repo.getQuestion(it)
    }

    fun setCategory(category:String){
        if (_category.value != category) {
            _category.value = category
        }
    }

}
