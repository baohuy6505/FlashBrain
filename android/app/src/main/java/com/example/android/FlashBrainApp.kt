package com.example.android

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FlashBrainApp : Application(), Configuration.Provider {

    // 👈 Hilt sẽ nhét cái xưởng sản xuất Worker vào đây
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory) // 👈 Nói cho WorkManager dùng xưởng của Hilt
            .build()
}