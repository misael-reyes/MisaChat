package com.example.misachat.domain.model

import java.util.*

data class Message(
    var message: String = "",
    var from: String = "",
    var dob: Date = Date()
)