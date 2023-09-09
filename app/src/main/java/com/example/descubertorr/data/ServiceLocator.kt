package com.example.descubertorr.data

import com.example.descubertorr.data.repositories.SiteRepository

object ServiceLocator {
    val realmManager = RealmManager()
    lateinit var siteRepository: SiteRepository
    /**
     * Call when user logged in and realm created
     */
    fun configureRealm(){
        requireNotNull(realmManager.realm)
        val realm = realmManager.realm!!
        siteRepository = SiteRepository(realm, realmManager.realmApp.currentUser!!.id)
    }

}