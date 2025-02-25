package com.example.golmokstar.utils

fun String.formatToDate(): String {
    if (this.length == 8) {
        return "${this.substring(0, 4)}-${this.substring(4, 6)}-${this.substring(6, 8)}"
    }
    return this
}