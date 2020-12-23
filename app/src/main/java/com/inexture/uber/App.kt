package com.inexture.uber

import android.app.Application
import androidx.room.Room
import com.chibatching.kotpref.Kotpref
import com.inexture.uber.localdb.AppDatabase
import com.uber.sdk.android.core.UberSdk
import com.uber.sdk.core.auth.Scope
import com.uber.sdk.core.auth.Scope.*
import com.uber.sdk.rides.client.SessionConfiguration
import java.util.*
import com.google.android.libraries.places.api.Places.createClient
import com.google.android.libraries.places.api.net.PlacesClient
import android.R.attr.apiKey
import com.google.android.libraries.places.api.Places


class App : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(applicationContext)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        initUber()
        initPlaceApi()
    }

    private fun initPlaceApi() {
        // Initialize Places.
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        // Create a new Places client instance.
        val placesClient = createClient(this)
    }

    private fun initUber() {
        val config = SessionConfiguration.Builder()
            // mandatory
            .setClientId("o_JGCCXAVVvpfitZyo0n8bVSD81UfYIQ")
            // required for implicit grant authentication
            .setRedirectUri("com.inexture.uber.uberauth://redirect")
            .setScopes(listOf(Scope.PROFILE, Scope.REQUEST))
            // optional: set sandbox as operating environment
            .setEnvironment(SessionConfiguration.Environment.SANDBOX)
            .build()

        UberSdk.initialize(config);
    }
}