package com.example.encryptiondatastore.data.model

import kotlinx.serialization.Serializable

/**
 * Holds the user's session. Empty strings = no active session.
 * This object is serialized to JSON then encrypted by AeadSerializer before being written to disk.
 */
@Serializable
data class SessionData(
    val email: String = "",
    val token: String = ""
)
