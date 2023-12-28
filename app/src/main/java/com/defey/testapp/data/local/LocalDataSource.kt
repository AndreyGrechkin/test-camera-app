package com.defey.testapp.data.local

import com.defey.testapp.data.local.model.CameraEntity
import com.defey.testapp.data.local.model.DoorEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LocalDataSource {
    suspend fun addCameraDB(camera: CameraEntity)
    suspend fun addDoorDB(door: DoorEntity)
    fun getCamerasDB(): Flow<List<CameraEntity>>
    fun getDoorsDB(): Flow<List<DoorEntity>>
    suspend fun updateCamera(camera: CameraEntity)
    suspend fun updateDoor(door: DoorEntity)
}

class LocalDataSourceImpl @Inject constructor(
    private val realm: Realm
) : LocalDataSource {
    override suspend fun addCameraDB(camera: CameraEntity) {
        realm.write { copyToRealm(camera, UpdatePolicy.ALL) }

    }

    override suspend fun addDoorDB(door: DoorEntity) {
        realm.write { copyToRealm(door, UpdatePolicy.ALL) }
    }

    override fun getCamerasDB(): Flow<List<CameraEntity>> {
        return realm.query<CameraEntity>().asFlow().map { it.list }
    }

    override fun getDoorsDB(): Flow<List<DoorEntity>> {
        return realm.query<DoorEntity>().asFlow().map { it.list }
    }

    override suspend fun updateCamera(camera: CameraEntity) {
        realm.write {
            val queriedCamera = query<CameraEntity>(query = "id == $0", camera.id).first().find()
            queriedCamera?.name = camera.name
        }
    }

    override suspend fun updateDoor(door: DoorEntity) {
        realm.write {
            val queriedDoor = query<DoorEntity>(query = "id == $0", door.id).first().find()
            queriedDoor?.name = door.name
        }
    }
}