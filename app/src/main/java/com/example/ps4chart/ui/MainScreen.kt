package com.example.ps4chart.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ps4chart.model.ExploitRow
import com.example.ps4chart.model.TocItem
import com.example.ps4chart.ml.TranslateDirection
import com.example.ps4chart.MainViewModel
import kotlinx.coroutines.launch
