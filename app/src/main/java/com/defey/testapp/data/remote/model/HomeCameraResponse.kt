package com.defey.testapp.data.remote.model

import com.defey.testapp.data.local.model.CameraEntity
import kotlinx.serialization.Serializable

@Serializable
data class HomeCameraResponse(
    val success: Boolean,
    val data: RoomCameraResponse
)

@Serializable
data class RoomCameraResponse(
    val room: List<String>,
    val cameras: List<CameraResponse>
)

@Serializable
data class CameraResponse(
    val name: String,
    val snapshot: String,
    val room: String?,
    val id: Int,
    val favorites: Boolean,
    val rec: Boolean
)

fun CameraResponse.toEntity(): CameraEntity {
    return CameraEntity().apply {
        name = this@toEntity.name
        snapshot = this@toEntity.snapshot
        room = this@toEntity.room
        id = this@toEntity.id
        favorites = this@toEntity.favorites
        rec = this@toEntity.rec
    }
}