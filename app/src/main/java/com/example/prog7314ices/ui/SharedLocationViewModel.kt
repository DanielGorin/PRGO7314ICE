package com.example.prog7314ices.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedLocationViewModel : ViewModel() {
    private val _locationKey = MutableLiveData<String>("305605") // Durban by default
    val locationKey: LiveData<String> = _locationKey
    fun updateLocationKey(newKey: String?) {
        if (!newKey.isNullOrBlank()) _locationKey.value = newKey
    }
}
