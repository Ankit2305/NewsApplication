package com.example.newsapplicationmoengage.di

import com.example.newsapplicationmoengage.repository.NewsRepository
import com.example.newsapplicationmoengage.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NewsRepository() }
    viewModel { NewsViewModel(get()) }
}