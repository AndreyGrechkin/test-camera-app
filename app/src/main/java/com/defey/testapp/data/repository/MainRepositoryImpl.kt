package com.defey.testapp.data.repository

import com.defey.testapp.data.local.LocalDataSource
import com.defey.testapp.data.remote.RemoteDataSource
import com.defey.testapp.data.remote.model.toEntity
import com.defey.testapp.domain.MainRepository
import com.defey.testapp.domain.Response
import com.defey.testapp.domain.model.Camera
import com.defey.testapp.domain.model.Door
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : MainRepository {
    override suspend fun synchronizeCamera(): Response<Unit> {
        return safeApiCall {
            val response = remote.getCameras().data.cameras
            response.forEach { camera ->
                local.addCameraDB(camera.toEntity())
            }
        }
    }

    override suspend fun synchronizeDoor(): Response<Unit> {
        return safeApiCall {
            val response = remote.getDoors().data
            response.forEach { door ->
                local.addDoorDB(door.toEntity())
            }
        }
    }

    override fun getCamera(): Flow<List<Camera>> {
        return local.getCamerasDB().map { list -> list.map { it.convertDomain() } }

    }

    override fun getDoor(): Flow<List<Door>> {
        return local.getDoorsDB().map { list -> list.map { it.convertDomain() } }
    }
}