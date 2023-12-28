package com.defey.testapp.domain.model

data class Door(
    val name: String,
    val snapshot: String?,
    val room: String?,
    val id: Int,
    val favorites: Boolean
)
