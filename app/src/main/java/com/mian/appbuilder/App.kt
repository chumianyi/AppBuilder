package com.mian.appbuilder

import android.app.Application
import com.mian.appbuilder.db.AppDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getInstance(this)
    }
}
