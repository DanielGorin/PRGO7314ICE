package com.example.prog7314ices.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedLocationViewModel : ViewModel() {
    private val _locationKey = MutableLiveData("305605")   // type inferred as MutableLiveData<String>
    val locationKey: LiveData<String> = _locationKey

    fun updateLocationKey(newKey: String?) {
        val key = newKey?.trim()
        if (key.isNullOrEmpty()) return
        _locationKey.value = requireNotNull(key)            // <- lint is happy: explicitly non-null
    }
}
