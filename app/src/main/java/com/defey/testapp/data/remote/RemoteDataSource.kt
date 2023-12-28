package com.defey.testapp.data.remote

import com.defey.testapp.data.remote.model.HomeCameraResponse
import com.defey.testapp.data.remote.model.HomeDoorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.utils.io.core.use
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getCameras(): HomeCameraResponse
    suspend fun getDoors(): HomeDoorResponse
}

class RemoteDataSourceImpl @Inject constructor(
    private val api: HttpClient
) : RemoteDataSource {
    override suspend fun getCameras(): HomeCameraResponse {
        return api.request("http://cars.cprogroup.ru/api/rubetek/cameras/")
            .body<HomeCameraResponse>()
    }

    override suspend fun getDoors(): HomeDoorResponse {
        return api.request("http://cars.cprogroup.ru/api/rubetek/doors/").body<HomeDoorResponse>()
    }
}
