package com.dicoding.alfistoryapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class SessionPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: SessionModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[EMAIL_KEY] = user.email
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    fun getSession(): Flow<SessionModel> {
        return dataStore.data.map { preferences ->
            SessionModel(
                preferences[ID_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreference? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}