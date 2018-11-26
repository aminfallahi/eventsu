package com.aminfallahi.eventsu.general

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.aminfallahi.eventsu.general.di.*
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class OpenEventGeneral : MultiDexApplication() {

    companion object {
        @JvmStatic
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin(this, listOf(commonModule, apiModule, viewModelModule, networkModule, databaseModule))
        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(applicationContext)
    }
}
