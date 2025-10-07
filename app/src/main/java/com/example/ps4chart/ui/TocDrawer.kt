package com.example.ps4chart.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ps4chart.model.TocItem

@Composable
fun TocDrawer(items: List<TocItem>, onSelect: (String) -> Unit) {
    Column {
        items.forEach { it ->
            Text(
                text = it.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(it.anchorId) }
                    .padding(12.dp)
            )
        }
    }
}
