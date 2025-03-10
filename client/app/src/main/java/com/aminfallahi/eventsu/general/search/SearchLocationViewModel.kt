package com.aminfallahi.eventsu.general.search

import android.arch.lifecycle.ViewModel
import com.aminfallahi.eventsu.general.data.Preference

class SearchLocationViewModel(private val preference: Preference) : ViewModel() {
    private val tokenKey = "LOCATION"

    fun saveSearch(query: String) {
        preference.putString(tokenKey, query)
    }
}
