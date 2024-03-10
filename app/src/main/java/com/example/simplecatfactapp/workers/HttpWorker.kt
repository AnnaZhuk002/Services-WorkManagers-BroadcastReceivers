package com.example.simplecatfactapp.workers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simplecatfactapp.data.Fact
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class HttpWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    var context = context
    val client = OkHttpClient()
    val gson = Gson()

    var fact: String = String()

    override suspend fun doWork(): Result {
        Log.d("WORKER", "ACTION")
        val request = Request.Builder()
            .url("https://catfact.ninja/fact")
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        val entity = gson.fromJson(response.body?.string() ?: String(), Fact::class.java)

        fact = entity.fact

        println(fact)

        val intent = Intent("com.example.simplecatfactapp.FACT_UPDATE") // Same action as in the receiver
        intent.putExtra("FACT", fact)
        context.sendBroadcast(intent)
        Log.d("SENT_TO_MAIN", "DONE")

        return Result.success()
    }

    val coroutineScope: CoroutineScope
        get() = CoroutineScope(Dispatchers.IO)
}