package com.example.ps4chart.ml

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translator
import kotlinx.coroutines.tasks.await

enum class TranslateDirection { EN_TO_RU, RU_TO_EN }

class TranslatorHelper {

    private var translatorENtoRU: Translator? = null
    private var translatorRUtoEN: Translator? = null

    private fun getOptions(dir: TranslateDirection): TranslatorOptions =
        when (dir) {
            TranslateDirection.EN_TO_RU -> TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()
            TranslateDirection.RU_TO_EN -> TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.RUSSIAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
        }

    private fun getTranslator(dir: TranslateDirection): Translator =
        when (dir) {
            TranslateDirection.EN_TO_RU -> translatorENtoRU
                ?: Translation.getClient(getOptions(dir)).also { translatorENtoRU = it }
            TranslateDirection.RU_TO_EN -> translatorRUtoEN
                ?: Translation.getClient(getOptions(dir)).also { translatorRUtoEN = it }
        }

    suspend fun ensureModelDownloaded(dir: TranslateDirection) {
        getTranslator(dir).downloadModelIfNeeded().await()
    }

    suspend fun translate(text: String, dir: TranslateDirection): String {
        return getTranslator(dir).translate(text).await()
    }

    fun close() {
        translatorENtoRU?.close()
        translatorRUtoEN?.close()
    }
}
