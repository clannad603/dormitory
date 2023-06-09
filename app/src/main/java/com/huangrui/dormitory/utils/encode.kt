package com.huangrui.dormitory.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * When you want to pass the URL in the URL,
 * please pay attention to the encoding format,
 * otherwise it will cause / be parsed by Navigation as the system directed URL.
 * Please use encode to encode the URL to avoid this problem
 * create by zyique chou 05/11/2022
 */

fun String.encode() = URLEncoder.encode(this, StandardCharsets.UTF_8.toString()) ?: ""

fun String.decode() = URLDecoder.decode(this, StandardCharsets.UTF_8.toString()) ?: ""
