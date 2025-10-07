package com.example.ps4chart.repository

import com.example.ps4chart.model.ExploitRow
import com.example.ps4chart.model.TocItem
import com.example.ps4chart.parser.HtmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ContentRepository(private val httpClient: OkHttpClient = OkHttpClient()) {

    private val parser = HtmlParser()

    suspend fun fetchHtml(url: String): String? = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder().url(url).get().build()
            httpClient.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext null
                resp.body?.string()
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun parseRows(html: String): List<ExploitRow> = withContext(Dispatchers.Default) {
        parser.parseTableRows(html)
    }

    suspend fun parseToc(html: String): List<TocItem> = withContext(Dispatchers.Default) {
        parser.parseToc(html)
    }
}
