package com.example.newsapplicationmoengage

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.newsapplicationmoengage.helper.MoeEventHelper
import com.example.newsapplicationmoengage.helper.SharedPreferencesHelper
import com.example.newsapplicationmoengage.helper.login.LocalLogin
import com.example.newsapplicationmoengage.view.NewsAdapter
import com.example.newsapplicationmoengage.viewmodel.NewsViewModel
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.pushbase.MoEPushHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val newsViewModel by viewModel<NewsViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val localLogin = LocalLogin(this)
    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        MoEPushHelper.getInstance().pushPermissionResponse(applicationContext, isGranted)
        var requestCount = SharedPreferencesHelper.getProperty(applicationContext, "notifPermissionRequestCount")?.toIntOrNull() ?: 0
        requestCount ++
        SharedPreferencesHelper.setProperty(applicationContext, "notifPermissionRequestCount", requestCount.toString())
        MoEPushHelper.getInstance().updatePushPermissionRequestCount(applicationContext, requestCount)
        Log.i("DebugTag", "Permission granted: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivityTag", "Logged In user = ${SharedPreferencesHelper.getProperty(this, SharedPreferencesHelper.LOGGED_IN_USER)}")
        MoeEventHelper.sendEvent(this, "MainActivity onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        setUpObservers()
        fetchAllNews()
//        initializeMoEngageSdk()
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
        MoeEventHelper.sendEvent(
            context = this,
            eventName = "Fetch News"
        )
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
                var sortOrder = SortOrder.INCREASING
                val sortDrawable = when(newsViewModel.sortNews()) {
                    SortOrder.INCREASING -> {
                        sortOrder = SortOrder.DECREASING
                        resources.getDrawable(R.drawable.ic_ascending, theme)
                    }
                    SortOrder.DECREASING -> {
                        sortOrder = SortOrder.DECREASING
                        resources.getDrawable(R.drawable.ic_descending, theme)
                    }
                }
                sortNewsFab.setImageDrawable(sortDrawable)
                MoeEventHelper.sendEvent(
                    context = this@MainActivity,
                    eventName = "Sort News",
                    "sortOrder" to sortOrder
                )
//                MoEAnalyticsHelper.setUniqueId(this@MainActivity, "1234_${System.currentTimeMillis()}")
            }

            logoutFab.setOnClickListener {
                localLogin.logout {
                    Log.i("MainActivityTag", "User logged out ...")

                    MoECoreHelper.logoutUser(this@MainActivity)

                    finish()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
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

    private fun initializeMoEngageSdk() {
        val moEngage = MoEngage.Builder(this.application, "EAUBZROL4WEPUSJDS814PDQO", DataCenter.DATA_CENTER_1)
            .configureNotificationMetaData(
                NotificationConfig(
                    smallIcon = R.drawable.ic_stat_name,
                    largeIcon = R.mipmap.ic_launcher,
                    notificationColor = R.color.moengage_primary,
                    isMultipleNotificationInDrawerEnabled = true
                )
            )
//            .configureFcm(FcmConfig(false))
            .configureLogs(LogConfig(level = LogLevel.VERBOSE, true))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)
    }
}