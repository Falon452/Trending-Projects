package com.falon.feed.presentation.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
