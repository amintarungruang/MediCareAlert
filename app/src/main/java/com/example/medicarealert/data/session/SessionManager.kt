package com.example.medicarealert.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.map

class SessionManager private constructor(private val appContext: Context) {

    private val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile("session") }
        )
    }

    companion object {
        // keys
        val KEY_ONBOARDED   = booleanPreferencesKey("is_onboarded")
        val KEY_GUEST       = booleanPreferencesKey("is_guest")
        val KEY_ACCOUNTID   = stringPreferencesKey("account_id")
        val KEY_DISPLAYNAME = stringPreferencesKey("display_name")

        @Volatile private var INSTANCE: SessionManager? = null
        fun getInstance(ctx: Context): SessionManager =
            INSTANCE ?: synchronized(this) {
                SessionManager(ctx.applicationContext).also { INSTANCE = it }
            }
    }

    // reads
    val isOnboarded = dataStore.data.map { it[KEY_ONBOARDED] ?: false }
    val isGuest     = dataStore.data.map { it[KEY_GUEST] ?: false }
    val accountId   = dataStore.data.map { it[KEY_ACCOUNTID] ?: "" }
    val displayName = dataStore.data.map { it[KEY_DISPLAYNAME] ?: "" }

    // writes
    suspend fun setGuest() {
        dataStore.edit {
            it[KEY_GUEST] = true
            it[KEY_ONBOARDED] = true
            it[KEY_ACCOUNTID] = ""
            it[KEY_DISPLAYNAME] = "ผู้เยี่ยมชม"
        }
    }

    suspend fun setUser(userId: Long, name: String) {
        dataStore.edit {
            it[KEY_GUEST] = false
            it[KEY_ONBOARDED] = true
            it[KEY_ACCOUNTID] = userId.toString()
            it[KEY_DISPLAYNAME] = name
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
