package com.example.ps4chart.model

data class ExploitRow(
    val id: String,
    val firmware: String,
    val userland: String,
    val kernel: String,
    val infoOriginal: String
)

data class TocItem(val title: String, val anchorId: String)
