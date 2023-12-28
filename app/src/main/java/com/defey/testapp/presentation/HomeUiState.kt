package com.defey.testapp.presentation

import com.defey.testapp.domain.model.Camera
import com.defey.testapp.domain.model.Door

data class HomeUiState(
    val cameraList: Map<String?, List<Camera>> = emptyMap(),
    val doorList: List<Door> = emptyList(),
    val loadCamera: Boolean = false,
    val loadDoor: Boolean = false,
)

data class Category(
    val name: String?,
    val items: List<Camera>
)


