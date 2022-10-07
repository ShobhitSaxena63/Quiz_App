package com.shobhit63.quizo.ui.question

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shobhit63.quizo.R
import com.shobhit63.quizo.data.questions.Questions
import com.shobhit63.quizo.databinding.FragmentQuestionScreenBinding
import timber.log.Timber



/**
 * A simple [Fragment] subclass.
 */

class QuestionScreen : Fragment() {

    private var _binding: FragmentQuestionScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: QuestionsViewModel
    private var category = ""
    private var questionNumber = 0
    private var score = 0
    private var userSubmit = ""
    private var userId = 0
    private lateinit var myCounter:CountDownTimer
    private var duration:Long = 30
    private var time:Long = 0

    companion object {
        private const val QUESTION_NUMBER = "QUESTION_NUMBER"
        private const val SCORE = "SCORE"
        private const val USER_SUBMIT = "userSubmit"
        private const val USER_ID = "userId"
        private const val TIME = "TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[QuestionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQuestionScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Function flow : onViewCreated")
        arguments?.let {
            category = QuestionScreenArgs.fromBundle(requireArguments()).category.toString()
        }


                savedInstanceState?.let {
            if (savedInstanceState.containsKey(QUESTION_NUMBER) && savedInstanceState.containsKey(SCORE)) {
                questionNumber = savedInstanceState.getInt(QUESTION_NUMBER)
                score = savedInstanceState.getInt(SCORE)
            }
                }
//            duration = 30
            savedInstanceState?.let {
            if (savedInstanceState.containsKey(TIME)) {
                time = savedInstanceState.getLong(TIME)
                duration = time
                Timber.d("CountDown Timer Restore : Duration = $duration | time = $time")
            } }




        viewModel.setCategory(category)
        viewModel.questionList.observe(viewLifecycleOwner) { questionList->
            questionList?.let {

                setQuestionsData(it)

                savedInstanceState?.let {
                    if (savedInstanceState.containsKey("userSubmit")) { //another if
                        userId = savedInstanceState.getInt("userId")
                        userSubmit = savedInstanceState.getString("userSubmit").toString()
                        if (userSubmit.isNotEmpty()) {
                            Timber.d("User is Not Empty $userSubmit ")
                            textViewDisable()
                            answerRestore(userId, questionList, questionNumber)
                        } } }
            }
        }

    }

    private fun quizTimer(questionList: List<Questions> , duration:Long){
        Timber.d("Function flow : quizTimer")
        myCounter =  object : CountDownTimer(duration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                 time = (millisUntilFinished / 1000)
                binding.timer.text = getString(R.string.timer_value, time)

            }
            override fun onFinish() {
                reset()
                questionNumber += 1
                if (questionNumber < questionList.size) {
                    quizTimer(questionList,30)
                    setQuestions(questionList, questionNumber)
                    if(questionNumber==9){
                        binding.nextbtn.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                        binding.nextbtn.text = getString(R.string.finish)
                    }
                } else {
                    findNavController().navigate(QuestionScreenDirections.actionQuestionScreenToScroreScreen(score,category))
                }

            }
        }
    }


    private fun setQuestionsData(questionList: List<Questions>) {
        Timber.d("Function flow : setQuestionsData")
        quizTimer(questionList,duration)
        setQuestions(questionList, questionNumber)
        val listener = View.OnClickListener { findId ->
            when (findId?.id) {
                R.id.nextbtn -> {
                    reset()
                    questionNumber += 1
                    if (questionNumber < questionList.size) {
                        quizTimer(questionList,30)
                        setQuestions(questionList, questionNumber)
                        if(questionNumber==questionList.size-1){
                            binding.nextbtn.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.red))
                            binding.nextbtn.text = getString(R.string.finish)
                        }

                    } else {
                        findNavController().navigate(QuestionScreenDirections.actionQuestionScreenToScroreScreen(score,category))
                    }
                }
                R.id.quit_btn -> {
                    //close timer
                    myCounter.cancel()
                    findNavController().navigate(R.id.action_questionScreen_to_categoryScreen)
                }
                R.id.option_one -> {
                    checkAnswer(binding.optionOne, questionList, questionNumber)
                }
                R.id.option_two -> {
                    checkAnswer(binding.optionTwo, questionList, questionNumber)
                }
                R.id.option_three -> {
                    checkAnswer(binding.optionThree, questionList, questionNumber)
                }
                R.id.option_four -> {
                    checkAnswer(binding.optionFour, questionList, questionNumber)
                }
            }
        }

        binding.optionOne.setOnClickListener(listener)
        binding.optionTwo.setOnClickListener(listener)
        binding.optionThree.setOnClickListener(listener)
        binding.optionFour.setOnClickListener(listener)
        binding.nextbtn.setOnClickListener(listener)
        binding.quitBtn.setOnClickListener(listener)


    }
//    private fun saveBestScore(){
//        val sharePref:SharedPreferences = requireActivity().getSharedPreferences("name",Context.MODE_PRIVATE)
//        with(sharePref.edit()){
//            putInt(category,score)
//            Timber.d("Best Score Category: $category    Score: $score")
//            commit()
//        }
//    }

    private fun setQuestions(questionList: List<Questions>, questionNumber: Int) {
        Timber.d("Function flow : setQuestions")
        myCounter.start()
        binding.question.text = questionList[questionNumber].question
        binding.optionOne.text = questionList[questionNumber].option_one
        binding.optionTwo.text = questionList[questionNumber].option_two
        binding.optionThree.text = questionList[questionNumber].option_three
        binding.optionFour.text = questionList[questionNumber].option_four
        val questionNumberIndicator = "${questionNumber + 1}/${questionList.size}"
        binding.quesNumber.text = questionNumberIndicator

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("Function flow : onSaveInstanceState")
        outState.putInt(QUESTION_NUMBER, questionNumber)
        outState.putInt(SCORE, score)
        outState.putString(USER_SUBMIT, userSubmit)
        outState.putInt(USER_ID, userId)
        outState.putLong(TIME,time)
        Timber.d("CountDown Timer onSaveInstanceState : Duration = $duration | time = $time")
    }


    private fun checkAnswer(submit: TextView, questionList: List<Questions>, questionNumber: Int) {
        Timber.d("Function flow : checkAnswer")
        userSubmit = submit.text.toString()
        userId = submit.id
        textViewDisable()
        val submitAnswer: String = submit.text.toString()
        if (submitAnswer == questionList[questionNumber].correct_answer) {
            submit.setBackgroundResource(R.drawable.correct_option_background)
            score += 1
        } else {
            submit.setBackgroundResource(R.drawable.wrong_answer_background)
            backgroundSet(questionList,questionNumber)
        }

    }

    private fun reset() {
        Timber.d("Function flow : reset")
        myCounter.cancel()
        binding.optionOne.setBackgroundResource(R.drawable.option_background)
        binding.optionTwo.setBackgroundResource(R.drawable.option_background)
        binding.optionThree.setBackgroundResource(R.drawable.option_background)
        binding.optionFour.setBackgroundResource(R.drawable.option_background)
        binding.optionOne.isClickable = true
        binding.optionTwo.isClickable = true
        binding.optionThree.isClickable = true
        binding.optionFour.isClickable = true
        userId = 0
        userSubmit = ""
    }

    private fun answerRestore(userAnswer: Int, questionList: List<Questions>, questionNumber: Int) {
        Timber.d("Function flow : answerRestore")
        val correctAnswer = questionList[questionNumber].correct_answer
        if (userAnswer == R.id.option_one) {
            if (questionList[questionNumber].option_one == correctAnswer) {
                binding.optionOne.setBackgroundResource(R.drawable.correct_option_background)
            } else {
                binding.optionOne.setBackgroundResource(R.drawable.wrong_answer_background)
                backgroundSet(questionList,questionNumber)
            }
        } else if (userAnswer == R.id.option_two) {
            if (questionList[questionNumber].option_two == correctAnswer) {
                binding.optionTwo.setBackgroundResource(R.drawable.correct_option_background)
            } else {
                binding.optionTwo.setBackgroundResource(R.drawable.wrong_answer_background)
                backgroundSet(questionList,questionNumber)
            }
        } else if (userAnswer == R.id.option_three) {
            if (questionList[questionNumber].option_three == correctAnswer) {
                binding.optionThree.setBackgroundResource(R.drawable.correct_option_background)
            } else {
                binding.optionThree.setBackgroundResource(R.drawable.wrong_answer_background)
                backgroundSet(questionList,questionNumber)
            }
        } else if (userAnswer == R.id.option_four) {
            if (questionList[questionNumber].option_four == correctAnswer) {
                binding.optionFour.setBackgroundResource(R.drawable.correct_option_background)
            } else {
                binding.optionFour.setBackgroundResource(R.drawable.wrong_answer_background)
                backgroundSet(questionList,questionNumber)
            }
        }
    }

    private fun backgroundSet(questionList: List<Questions>, questionNumber: Int) {
        Timber.d("Function flow : backgroundSet")
        val correctAnswer = questionList[questionNumber].correct_answer
        if (questionList[questionNumber].option_one == correctAnswer) {
            binding.optionOne.setBackgroundResource(R.drawable.correct_option_background)
        } else if (questionList[questionNumber].option_two == correctAnswer) {
            binding.optionTwo.setBackgroundResource(R.drawable.correct_option_background)
        } else if (questionList[questionNumber].option_three == correctAnswer) {
            binding.optionThree.setBackgroundResource(R.drawable.correct_option_background)
        } else if (questionList[questionNumber].option_four == correctAnswer) {
            binding.optionFour.setBackgroundResource(R.drawable.correct_option_background)
        }
    }

    private fun textViewDisable() {
        Timber.d("Function flow : textViewDisable")
        binding.optionOne.isClickable = false
        binding.optionTwo.isClickable = false
        binding.optionThree.isClickable = false
        binding.optionFour.isClickable = false
    }

    override fun onStop() {
        super.onStop()
        Timber.d("Function flow : onStop")
        myCounter.cancel()
    }

}
