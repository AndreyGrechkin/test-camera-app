package com.defey.testapp.data.remote.model

import com.defey.testapp.data.local.model.DoorEntity
import kotlinx.serialization.Serializable

@Serializable
data class HomeDoorResponse(
    val success: Boolean,
    val data: List<DoorResponse>
)

@Serializable
data class DoorResponse(
    val name: String,
    val snapshot: String? = null,
    val room: String?,
    val id: Int,
    val favorites: Boolean
)

fun DoorResponse.toEntity(): DoorEntity {
    return DoorEntity().apply {
        name = this@toEntity.name
        snapshot = this@toEntity.snapshot
        room = this@toEntity.room
        id = this@toEntity.id
        favorites = this@toEntity.favorites
    }
}