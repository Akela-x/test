package com.example.ps4chart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ps4chart.ml.TranslatorHelper
import com.example.ps4chart.repository.ContentRepository
import com.example.ps4chart.ui.MainScreen
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {

    private val repo = ContentRepository(OkHttpClient())
    private val translatorHelper = TranslatorHelper()
    private val vm = MainViewModel(repo, translatorHelper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(vm)
        }

        vm.loadFromUrl("https://consolemods.org/wiki/PS4:Exploit_Chart")
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onCleared()
    }
}
