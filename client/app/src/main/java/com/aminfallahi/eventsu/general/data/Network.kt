package com.aminfallahi.eventsu.general.data

import android.content.Context
import android.net.ConnectivityManager
import com.aminfallahi.eventsu.general.OpenEventGeneral

class Network {

    private val context by lazy {
        OpenEventGeneral.appContext
    }

    private val connectivityManager by lazy {
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }

    fun isNetworkConnected(): Boolean {
        return connectivityManager?.activeNetworkInfo != null
    }
}
