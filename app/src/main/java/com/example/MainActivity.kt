package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.HeliosAppContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HeliosViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true, dynamicColor = false) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = Color(0xFF000000)
        ) {
          val viewModel: HeliosViewModel = viewModel()
          HeliosAppContainer(viewModel = viewModel)
        }
      }
    }
  }
}

