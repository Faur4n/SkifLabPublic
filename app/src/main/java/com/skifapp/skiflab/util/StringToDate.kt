package com.skifapp.skiflab.util

import java.text.SimpleDateFormat
import java.util.*

fun String.parseDate(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): Date? {
    return kotlin.runCatching {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.parse(this)
    }.getOrNull()
}