package com.example.encryptiondatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.encryptiondatastore.navigation.AppNavGraph
import com.example.encryptiondatastore.ui.theme.EncryptionDataStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EncryptionDataStoreTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}