package com.example.ps4chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ps4chart.ml.TranslatorHelper
import com.example.ps4chart.ml.TranslateDirection
import com.example.ps4chart.model.ExploitRow
import com.example.ps4chart.model.TocItem
import com.example.ps4chart.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: ContentRepository,
    private val translator: TranslatorHelper
) : ViewModel() {

    private val _rows = MutableStateFlow<List<ExploitRow>>(emptyList())
    val rows: StateFlow<List<ExploitRow>> = _rows

    private val _toc = MutableStateFlow<List<TocItem>>(emptyList())
    val toc: StateFlow<List<TocItem>> = _toc

    private val _translations = MutableStateFlow<Map<String, String>>(emptyMap())
    val translations: StateFlow<Map<String, String>> = _translations

    private val _isTranslating = MutableStateFlow<Boolean>(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating

    var translationEnabled = true
    var translationDirection = TranslateDirection.EN_TO_RU

    fun loadFromUrl(url: String) {
        viewModelScope.launch {
            val html = repo.fetchHtml(url) ?: return@launch
            val parsedRows = repo.parseRows(html)
            _rows.value = parsedRows
            _toc.value = repo.parseToc(html)
            if (translationEnabled) startTranslateAll(parsedRows)
        }
    }

    private fun startTranslateAll(parsedRows: List<ExploitRow>) {
        viewModelScope.launch {
            _isTranslating.value = true
            try {
                translator.ensureModelDownloaded(translationDirection)
                val map = mutableMapOf<String, String>()
                for (r in parsedRows) {
                    val src = r.infoOriginal
                    val tr = if (src.isBlank()) "" else translator.translate(src, translationDirection)
                    map[r.id] = tr
                }
                _translations.value = map
            } catch (e: Exception) {
            } finally {
                _isTranslating.value = false
            }
        }
    }

    fun toggleTranslation(enabled: Boolean) {
        translationEnabled = enabled
        if (!enabled) _translations.value = emptyMap()
        else startTranslateAll(_rows.value)
    }

    fun changeDirection(dir: TranslateDirection) {
        translationDirection = dir
        if (translationEnabled) startTranslateAll(_rows.value)
    }

    override fun onCleared() {
        super.onCleared()
        translator.close()
    }
}
