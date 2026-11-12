package com.example.seldonfleet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.seldonfleet.data.local.AssetFileReader
import com.example.seldonfleet.data.local.FavoritesDataStore
import com.example.seldonfleet.data.repository.FleetRepositoryImpl
import com.example.seldonfleet.ui.navigation.AppNavHost
import com.example.seldonfleet.ui.theme.SeldonFleetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = FleetRepositoryImpl(AssetFileReader(applicationContext), FavoritesDataStore(applicationContext))
        setContent {
            SeldonFleetTheme(dynamicColor = false) {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { AppNavHost(repository) }
            }
        }
    }
}
