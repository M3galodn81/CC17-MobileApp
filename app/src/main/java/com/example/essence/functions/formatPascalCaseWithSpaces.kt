package com.example.essence.functions

fun formatTitleCaseWithSpaces(text: String): String {
    return text.replace(Regex("(?<=[a-z])(?=[A-Z])"), " ")
}
