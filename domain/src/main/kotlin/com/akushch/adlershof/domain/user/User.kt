package com.akushch.adlershof.domain.user

import arrow.core.Option
import java.util.UUID

data class User(
    val id: UUID,
    val email: String
) { companion object }

data class UserRegistration(val email: String, val password: String)

data class ValidUserRegistration(
    val id: UUID,
    val email: String
)

data class UserUpdate(
    val username: Option<String>,
    val email: Option<String>,
    val password: Option<String>,
    val bio: Option<String>,
    val image: Option<String>
)

data class ValidUserUpdate(
    val email: String
)
