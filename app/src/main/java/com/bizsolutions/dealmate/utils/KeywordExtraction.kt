package com.bizsolutions.dealmate.utils

import com.chaquo.python.Python

fun extractKeywords(text: String): List<String>? {
    val py = Python.getInstance()
    val keywordsModule = py.getModule("keywords")
    val extractKeywordsFunction = keywordsModule["extract_keywords"]

    val keywordsRaw = extractKeywordsFunction?.call(text)
    val keywords = keywordsRaw?.asList()?.map { it.toJava(String::class.java) }
    return keywords
}