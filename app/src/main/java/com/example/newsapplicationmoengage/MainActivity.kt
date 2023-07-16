package com.example.newsapplicationmoengage

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapplicationmoengage.constant.SortOrder
import com.example.newsapplicationmoengage.databinding.ActivityMainBinding
import com.example.newsapplicationmoengage.view.NewsAdapter
import com.example.newsapplicationmoengage.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val newsViewModel by viewModel<NewsViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.i("DebugTag", "Permission granted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        setUpObservers()
        fetchAllNews()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    /**
     * Sets up observers for newsLiveData and errorMessageLiveData from the NewsViewModel.
     */
    private fun setUpObservers() {
        newsViewModel.newsLiveData.observe(this) { news ->
            newsAdapter.submitList(news)
            binding.apply {
                newsRecyclerView.visibility = View.VISIBLE
                sortNewsFab.visibility = View.VISIBLE
            }
        }

        newsViewModel.errorMessageLiveData.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showErrorMessage(errorMessage)
            }
        }
    }

    /**
     * Fetches all news articles from the NewsViewModel.
     */
    private fun fetchAllNews() {
        newsViewModel.fetchAllNews()
        binding.apply {
            sortNewsFab.visibility = View.GONE
            newsRecyclerView.visibility = View.GONE
        }
    }

    /**
     * Sets up the view, including initializing the newsAdapter, configuring the RecyclerView,
     * and setting up the click listener for the sortNewsFab.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpView() {
        newsAdapter = NewsAdapter(this)
        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }
        binding.apply {
            sortNewsFab.setOnClickListener {
                val sortDrawable = when(newsViewModel.sortNews()) {
                    SortOrder.INCREASING -> {
                        resources.getDrawable(R.drawable.ic_ascending, theme)
                    }
                    SortOrder.DECREASING -> {
                        resources.getDrawable(R.drawable.ic_descending, theme)
                    }
                }
                sortNewsFab.setImageDrawable(sortDrawable)
            }
        }
    }

    /**
     * Shows an error message using a Toast.
     *
     * @param message The error message to display.
     */
    private fun showErrorMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}