package com.defey.testapp.domain

import com.defey.testapp.domain.model.Camera
import com.defey.testapp.domain.model.Door
import kotlinx.coroutines.flow.Flow

interface MainRepository : SafeApiCall {
    suspend fun synchronizeCamera(): Response<Unit>
    suspend fun synchronizeDoor(): Response<Unit>
    fun getCamera(): Flow<List<Camera>>
    fun getDoor(): Flow<List<Door>>
}