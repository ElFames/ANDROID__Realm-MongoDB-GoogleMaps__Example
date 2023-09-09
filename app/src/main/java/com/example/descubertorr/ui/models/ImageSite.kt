package com.example.descubertorr.ui.models

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.jetbrains.annotations.NotNull

open class ImageSite(
    @PrimaryKey @NotNull
    var _id: ObjectId = ObjectId.create(),
    var imageName: String,
    var picture: ByteArray? = null,
    var owner_id: String = "",
) : RealmObject {
    constructor() : this(imageName = "", owner_id = "")
}