package com.defey.testapp.data.local.model

import com.defey.testapp.domain.model.Camera
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CameraEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var snapshot: String? = null
    var room: String? = null
    var favorites: Boolean = false
    var rec: Boolean = false

    fun convertDomain() = Camera(
        id = id,
        name = name,
        snapshot = snapshot,
        room = room,
        favorites = favorites,
        rec = rec
    )
}
