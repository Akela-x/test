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
        when(dir) {
            TranslateDirection.EN_TO_RU -> TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.RUSSIAN)
                .build()
            TranslateDirection.RU_TO_EN -> TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.RUSSIAN)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
        }

    fun getTranslator(dir: TranslateDirection): Translator {
        return when (dir) {
            TranslateDirection.EN_TO_RU -> {
                if (translatorENtoRU == null) translatorENtoRU = Translation.getClient(getOptions(dir))
                translatorENtoRU!!
            }
            TranslateDirection.RU_TO_EN -> {
                if (translatorRUtoEN == null) translatorRUtoEN = Translation.getClient(getOptions(dir))
                translatorRUtoEN!!
            }
        }
    }

    suspend fun ensureModelDownloaded(dir: TranslateDirection) {
        val t = getTranslator(dir)
        t.downloadModelIfNeeded().await()
    }

    suspend fun translate(text: String, dir: TranslateDirection): String {
        val t = getTranslator(dir)
        return t.translate(text).await()
    }

    fun close() {
        translatorENtoRU?.close()
        translatorRUtoEN?.close()
    }
}
