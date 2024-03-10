package com.example.simplecatfactapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.simplecatfactapp.workers.HttpWorker
import java.util.concurrent.TimeUnit

class HttpService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("SERVICE", "ACTION")

        val workRequest = OneTimeWorkRequest.Builder(HttpWorker::class.java)
            .setInitialDelay(2L, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)

        stopSelf()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Очистка ресурсов
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}