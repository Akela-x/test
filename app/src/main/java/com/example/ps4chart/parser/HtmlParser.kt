package com.example.ps4chart.parser

import com.example.ps4chart.model.ExploitRow
import com.example.ps4chart.model.TocItem
import org.jsoup.Jsoup

class HtmlParser {

    fun parseTableRows(html: String): List<ExploitRow> {
        val doc = Jsoup.parse(html)
        val list = mutableListOf<ExploitRow>()
        val trs = doc.select("tbody tr")
        for (tr in trs) {
            val id = tr.id().ifBlank { "row_${list.size}" }
            val tds = tr.select("td")
            if (tds.size >= 4) {
                val firmware = tds[0].text().trim()
                val userland = tds[1].html().trim()
                val kernel = tds[2].text().trim()
                val info = tds[3].text().trim()
                list.add(ExploitRow(id, firmware, userland, kernel, info))
            }
        }
        return list
    }

    fun parseToc(html: String): List<TocItem> {
        val doc = Jsoup.parse(html)
        val toc = mutableListOf<TocItem>()
        val anchors = doc.select(".toc a[href^=#]")
        if (anchors.isNotEmpty()) {
            for (a in anchors) {
                val title = a.text().ifBlank { a.attr("href") }
                val anchor = a.attr("href").removePrefix("#")
                toc.add(TocItem(title, anchor))
            }
        } else {
            val heads = doc.select("h1,h2,h3").filter { it.id().isNotBlank() }
            for (h in heads) toc.add(TocItem(h.text(), h.id()))
        }
        return toc
    }
}
