package com.aminfallahi.eventsu.general.search

import android.arch.lifecycle.ViewModel
import com.aminfallahi.eventsu.general.data.Preference

class SearchTimeViewModel(private val preference: Preference) : ViewModel() {
    private val tokenKeyDate = "DATE"
    private val tokenKeyNextDate = "NEXT_DATE"

    fun saveDate(time: String) {
        preference.putString(tokenKeyDate, time)
    }

    fun saveNextDate(time: String) {
        preference.putString(tokenKeyNextDate, time)
    }
}
