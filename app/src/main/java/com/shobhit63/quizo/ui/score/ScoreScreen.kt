package com.shobhit63.quizo.ui.score

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shobhit63.quizo.R
import com.shobhit63.quizo.databinding.FragmentScroreScreenBinding
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */
class ScoreScreen : Fragment() {

    private var _binding: FragmentScroreScreenBinding?=null
    private val binding get() = _binding!!
    private var score = 0
    private var category = " "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentScroreScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            score = ScoreScreenArgs.fromBundle(requireArguments()).score
            category =  ScoreScreenArgs.fromBundle(requireArguments()).category
        }
        saveBestScore()
        binding.finalScore.text = requireActivity().getString(R.string.final_score, score)
        when(score) {
            0 -> binding.message.text = getString(R.string.try_again)
            in 1.. 3-> binding.message.text = getString(R.string.keep_learning)
            in 4.. 7-> binding.message.text = getString(R.string.well_done)
            in 8..9-> binding.message.text = getString(R.string.excellent)
             10 -> binding.message.text = getString(R.string.superb)
            else -> binding.message.text = getString(R.string.well_done)
        }
        binding.replay.setOnClickListener {
            findNavController().navigate(R.id.action_scroreScreen_to_category2)
        }
    }


    private fun saveBestScore(){
         val sharePref: SharedPreferences = requireActivity().getSharedPreferences("bestScore", Context.MODE_PRIVATE)
             val bestScore = sharePref.getInt(category,0)
              if(bestScore<score){
                  with(sharePref.edit()){
                      putInt(category,score)
                      Timber.d("Best Score Category: $category    Score: $score")
                      commit()
                  }
              }

    }
}


