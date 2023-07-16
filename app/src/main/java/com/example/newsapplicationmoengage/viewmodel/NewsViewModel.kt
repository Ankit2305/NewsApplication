package com.example.newsapplicationmoengage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplicationmoengage.constant.SortOrder
import com.example.newsapplicationmoengage.constant.ViewType
import com.example.newsapplicationmoengage.constant.ViewType.getNewShimmeringModel
import com.example.newsapplicationmoengage.model.News
import com.example.newsapplicationmoengage.remote.ApiResponseObject
import com.example.newsapplicationmoengage.repository.NewsRepository
import kotlinx.coroutines.launch


/**
 * ViewModel class responsible for managing and providing news data to the UI.
 *
 * @param repository The repository class that handles fetching news data.
 */
class NewsViewModel(private val repository: NewsRepository): ViewModel() {

    private var originalNews: List<News>? = null

    /**
     * LiveData representing the list of news articles.
     */
    private val _newsLiveData = MutableLiveData<List<News>>()
    val newsLiveData: LiveData<List<News>> = _newsLiveData

    /**
     * LiveData representing the error message in case of an API error.
     */
    private val _errorMessageLiveData = MutableLiveData<String?>()
    val errorMessageLiveData: LiveData<String?> = _errorMessageLiveData

    /**
     * Current sort order of the news list.
     */
    private var sortOrder = SortOrder.DECREASING

    /**
     * Fetches all news articles from the repository.
     */
    fun fetchAllNews() {
        viewModelScope.launch {
            postShimmeringModel()
            when(val newsWrapper = repository.fetchAllNews()) {
                is ApiResponseObject.Success -> {
                    newsWrapper.data?.articles?.let { news ->
                        _newsLiveData.postValue(news)
                        originalNews = news
                    }
                }
                is ApiResponseObject.Error -> {
                    newsWrapper.exception.message.let { errorMessage ->
                        _errorMessageLiveData.postValue(errorMessage)
                    }
                }
            }
        }
    }

    private fun postShimmeringModel() {
        _newsLiveData.postValue(listOf(
            getNewShimmeringModel(),
            getNewShimmeringModel(),
            getNewShimmeringModel()
        ))
    }

    /**
     * Sorts the news articles based on the current sort order.
     *
     * @return The updated sort order after sorting.
     */
    fun sortNews(): SortOrder {
        val sortedNews = when(sortOrder) {
            SortOrder.INCREASING -> {
                sortOrder = SortOrder.DECREASING
                originalNews?.sortedByDescending { it.publishedAt }
            }
            SortOrder.DECREASING -> {
                sortOrder = SortOrder.INCREASING
                originalNews?.sortedBy { it.publishedAt }
            }
        }
        sortedNews?.let { news ->
            _newsLiveData.postValue(news)
        }
        return sortOrder
    }

}