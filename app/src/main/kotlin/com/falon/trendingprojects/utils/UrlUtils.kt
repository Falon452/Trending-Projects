package com.falon.trendingprojects.utils

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal fun String.decodeUrl(): String = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
