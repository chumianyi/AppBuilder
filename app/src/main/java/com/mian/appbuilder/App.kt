package com.mian.appbuilder

import android.app.Application
import com.mian.appbuilder.db.AppDatabase

class App : Application() {
    lateinit var database: AppDatabase
        private set

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getInstance(this)
    }
}
