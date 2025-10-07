package com.example.ps4chart.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ps4chart.model.ExploitRow
import com.example.ps4chart.model.TocItem
import com.example.ps4chart.ml.TranslateDirection
import com.example.ps4chart.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberScaffoldState()
    val rows by vm.rows.collectAsState()
    val toc by vm.toc.collectAsState()
    val translations by vm.translations.collectAsState()
    val isTranslating by vm.isTranslating.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var useDark by remember { mutableStateOf(true) }
    var translationOn by remember { mutableStateOf(vm.translationEnabled) }
    var direction by remember { mutableStateOf(vm.translationDirection) }
    var lang by remember { mutableStateOf("en") }

    val listState = rememberLazyListState()

    MaterialTheme(colors = if (useDark) darkColors() else lightColors()) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(title = { Text("PS4 Exploit Chart") }, actions = {
                    IconButton(onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                })
            },
            drawerContent = {
                Text("Оглавление", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.h6)
                Divider()
                TocDrawer(toc) { anchorId ->
                    val index = rows.indexOfFirst { it.id == anchorId }
                    if (index >= 0) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                            scaffoldState.drawerState.close()
                        }
                    } else {
                        Toast.makeText(context, "Section not found locally", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Theme", modifier = Modifier.padding(end = 6.dp))
                        Switch(checked = useDark, onCheckedChange = { useDark = it })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Translation", modifier = Modifier.padding(start = 12.dp, end = 6.dp))
                        Switch(checked = translationOn, onCheckedChange = {
                            translationOn = it
                            vm.toggleTranslation(it)
                        })
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        val newDir = if (direction == TranslateDirection.EN_TO_RU) TranslateDirection.RU_TO_EN else TranslateDirection.EN_TO_RU
                        direction = newDir
                        vm.changeDirection(newDir)
                    }) {
                        Text(if (direction == TranslateDirection.EN_TO_RU) "EN → RU" else "RU → EN")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Interface")
                    Button(onClick = { lang = if (lang == "en") "ru" else "en" }) {
                        Text(if (lang == "en") "English" else "Русский")
                    }
                }

                if (isTranslating) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(rows) { _, item ->
                        ExploitCard(item, translations[item.id])
                    }
                }
            }
        }
    }
}
