package com.defey.testapp.data.local.model

import com.defey.testapp.domain.model.Door
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class DoorEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var snapshot: String? = null
    var room: String? = null
    var favorites: Boolean = false

    fun convertDomain() = Door(
        id = id,
        name = name,
        snapshot = snapshot,
        room = room,
        favorites = favorites
    )
}


