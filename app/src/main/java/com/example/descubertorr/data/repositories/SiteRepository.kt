package com.example.descubertorr.data.repositories

import com.example.descubertorr.data.ServiceLocator
import com.example.descubertorr.ui.models.Site
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class SiteRepository(val realm: Realm, val userId: String) {
    var allSites =  realm.query<Site>().find()

    fun insertSite(name: String, imageName: String, description: String, latitud: String, longitud: String) {
        val site = Site(
            name = name,
            imageName = imageName,
            description = description,
            latitud = latitud,
            longitud = longitud,
            ownerId = userId
        )

        realm.writeBlocking {
            copyToRealm(site)
        }
    }
}