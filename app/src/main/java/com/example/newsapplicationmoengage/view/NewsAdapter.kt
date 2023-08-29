package com.example.newsapplicationmoengage.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplicationmoengage.constant.ViewType
import com.example.newsapplicationmoengage.databinding.NewsSingleItemBinding
import com.example.newsapplicationmoengage.databinding.NewsSingleItemShimmerBinding
import com.example.newsapplicationmoengage.helper.DateParser
import com.example.newsapplicationmoengage.helper.MoeEventHelper
import com.example.newsapplicationmoengage.model.News

/**
 * Adapter class responsible for binding news data to the UI.
 *
 * @param context The context of the activity.
 */
class NewsAdapter(private val context: Context): ListAdapter<News, NewsAdapter.ViewHolder>(UserItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ViewType.NEWS -> {
                val binding = NewsSingleItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NewsViewHolder(binding)
            }
            else -> {
                val binding = NewsSingleItemShimmerBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent,
                    false
                )
                ShimmerViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    /**
     * DiffUtil callback for comparing news items.
     */
    object UserItemDiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem

    }

    abstract inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bindTo(news: News)
    }

    /**
     * ViewHolder class representing a single item view in the RecyclerView.
     *
     * @param binding The data binding object for the item layout.
     */
    inner class NewsViewHolder(private val binding: NewsSingleItemBinding): ViewHolder(binding.root) {

        /**
         * Binds the news item data to the UI elements.
         *
         * @param news The news item to bind.
         */
        override fun bindTo(news: News) {
            binding.apply {
                title.text = news.title
                description.text = news.description
                publishedAt.text = DateParser.parseDate(news.publishedAt)
                Glide.with(context).load(news.urlToImage).into(image)
                root.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                    context.startActivity(browserIntent)
                }
                MoeEventHelper.sendEvent(
                    context = context,
                    eventName = "News Card Viewed",
                    "title" to news.title,
                    "description" to news.description,
                    "publishedAt" to news.publishedAt,
                    "urlToImage" to news.urlToImage,
                    "url" to news.url
                )
            }
        }
    }

    inner class ShimmerViewHolder(private val binding: NewsSingleItemShimmerBinding): ViewHolder(binding.root) {
        override fun bindTo(news: News) { }
    }
}