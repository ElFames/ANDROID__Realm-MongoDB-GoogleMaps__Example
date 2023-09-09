package com.example.descubertorr.data

import android.util.Log
import com.example.descubertorr.ui.models.Site
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.*
import io.realm.kotlin.mongodb.sync.SyncConfiguration

class RealmManager {
    val realmApp = App.create(AppConfiguration.Builder("descubertor-qzvis").log(LogLevel.ALL).build())
    var realm : Realm? = null
    var currentUser: User? = null

    fun loggedIn() = realmApp.currentUser?.loggedIn ?: false

    suspend fun configureRealm(){
        requireNotNull(realmApp.currentUser)

        val user = realmApp.currentUser!!
        val config = SyncConfiguration.Builder(user, setOf(Site::class))
            .initialSubscriptions{realm ->
                add(
                    realm.query<Site>(), "All Sports"
                )
            }
            .waitForInitialRemoteData()
            .build()
        realm = Realm.open(config)
        realm!!.subscriptions.waitForSynchronization()
        ServiceLocator.configureRealm()
    }

    suspend fun register(username: String, password: String) {
        try {
            realmApp.emailPasswordAuth.registerUser(username, password)
            login(username,password)
        } catch (e: Exception) {
            Log.e("REGISTER","RealmManager.register() -> ${e.message}", e)
        }
    }
    suspend fun login(username: String, password: String) {
        try {
            val creds = Credentials.emailPassword(username, password)
            realmApp.login(creds)
            configureRealm()
        } catch (e: Exception) {
            Log.e("LOGIN","RealmManager.login() -> ${e.message}", e)
        }
    }
}