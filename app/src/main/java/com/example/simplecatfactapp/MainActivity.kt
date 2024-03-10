package com.example.simplecatfactapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simplecatfactapp.services.HttpService

class MainActivity : ComponentActivity() {

    private val receiver = MyBroadcastReceiver()
    private lateinit var navController: NavHostController
    private var currentFact by mutableStateOf("")

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(
            receiver,
            IntentFilter("com.example.simplecatfactapp.FACT_UPDATE"),
            RECEIVER_EXPORTED
        )

        setContent {
            navController = rememberNavController()
            MainScreen(navController)
        }

    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val fact = intent.getStringExtra("FACT")
            // Update the text with the received fact
            currentFact = fact.orEmpty() // Handle potential null value
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun startServiceOnClick(navController: NavHostController) {
        val intent = Intent(applicationContext, HttpService::class.java)
        Log.d("start_service", "Started")
        startService(intent)
        navController.navigate("fact_screen")
    }

    @Composable
    fun RunServiceScreen(navController: NavHostController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Run service",
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { startServiceOnClick(navController) },
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Run")
            }
        }
    }

    @Composable
    fun FactScreen(navController: NavHostController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = currentFact)
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "service_run") {
            composable("service_run") {
                RunServiceScreen(navController)
            }
            composable("fact_screen") {
                FactScreen(navController)
            }
        }
    }

}