package com.omnilog.eurosport.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omnilog.eurosport.home.usecase.HomeUseCase

class HomeViewModelFactory(
    private val homeUseCase: HomeUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            HomeViewModel(homeUseCase) as T
        else
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}