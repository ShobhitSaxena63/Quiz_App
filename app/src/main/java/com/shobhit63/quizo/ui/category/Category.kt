package com.shobhit63.quizo.ui.category

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shobhit63.quizo.R
import com.shobhit63.quizo.databinding.FragmentCategoryBinding
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 */



class Category : Fragment() {
    private var _binding:FragmentCategoryBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel:TopicViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TopicViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("Hi! implementing timber")

        val categoryList = view.findViewById<RecyclerView>(R.id.category_list).apply{
            val orientation = resources.configuration.orientation
            var spanCount = 2
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                spanCount = 4
            }
            layoutManager = GridLayoutManager(activity , spanCount)
            adapter = CategoryAdapter {
                findNavController().navigate(CategoryDirections.actionCategory2ToQuestionScreen(it.topic,it.id))
            }
            setHasFixedSize(true)
        }
        viewModel.topicList.observe(viewLifecycleOwner) {
            (categoryList.adapter as CategoryAdapter).submitList(it)
        }

    }

}

