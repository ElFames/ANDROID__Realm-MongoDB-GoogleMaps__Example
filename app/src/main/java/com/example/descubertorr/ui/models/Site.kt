package com.example.descubertorr.ui.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.jetbrains.annotations.NotNull

open class Site (
    @PrimaryKey @NotNull
    var _id: ObjectId = ObjectId.create(),
    var name: String,
    var imageName: String,
    var description: String,
    var latitud: String,
    var longitud: String,
    var ownerId: String
): RealmObject {
    constructor() : this(name = "", imageName = "", description = "", latitud = "", longitud = "", ownerId = "")
    // var doAfter: RealmList<Site>? = realmListOf()
    override fun toString() = "Site($name, $imageName, $description, $latitud, $longitud, $ownerId)"
}
