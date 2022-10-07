package com.shobhit63.quizo.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shobhit63.quizo.R
import com.shobhit63.quizo.data.category.Topics
import timber.log.Timber


class CategoryAdapter(private val listener:(Topics)->Unit):androidx.recyclerview.widget.
ListAdapter<Topics, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    private var context: Context? = null

    inner class CategoryViewHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        private val topicImage:ImageView = containerView.findViewById(R.id.pic)
        private val topicTitle: TextView = containerView.findViewById(R.id.topic_name)
        private val bestScoreTv: TextView = containerView.findViewById(R.id.best_score)

        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition))
            }
        }

        fun bind(categoryData: Topics) {
            with(categoryData) {
               val setImage =  when(topic) {
                   "Capital"->R.drawable.indiaflag
                    "Galaxy"->R.drawable.galaxy
                    "First in India"->R.drawable.first_india
                    "Currency"->R.drawable.currency
                    "Country Capital"->R.drawable.country_capital
                    "Physics"->R.drawable.physics
                    "Biology"->R.drawable.study_of
                    "Important Dates"->R.drawable.important_dates
                   else -> {R.drawable.error}
               }
                Glide.with(containerView)
                    .load(setImage)
                    .error(R.drawable.error)
                    .into(topicImage)

                topicTitle.text = topic
                val sharedPref  = context!!.getSharedPreferences("bestScore",Context.MODE_PRIVATE)
                val bestScore = sharedPref.getInt(topic,0)
                Timber.d("Adapter Best Scores, $topic = $bestScore")
                bestScoreTv.append(bestScore.toString())

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(R.layout.category_look, parent, false)
        context = parent.context
        return CategoryViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}



class DiffCallback:DiffUtil.ItemCallback<Topics>() {
    override fun areItemsTheSame(oldItem: Topics, newItem: Topics): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Topics, newItem: Topics): Boolean {
        return oldItem == newItem
    }
}