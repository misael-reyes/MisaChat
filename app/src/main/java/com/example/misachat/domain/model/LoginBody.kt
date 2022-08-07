package com.example.misachat.domain.model

import com.example.misachat.iu.listOfChats.ProviderType

data class LoginBody(
    val email: String = "",
    val password: String = "",
    val provider: ProviderType = ProviderType.BASIC
)