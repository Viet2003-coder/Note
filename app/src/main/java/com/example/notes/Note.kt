package com.example.notes

data class Note(
    var id: String ?=null,
    var username: String ?= null,
    var title: String ?=null,
    var content: String ?= null,
    var timestamp: Long = System.currentTimeMillis()
) {
}