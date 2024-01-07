package com.dicoding.alfistoryapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.alfistoryapp.data.preferences.SessionPreference
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
object Injection {
    fun provideRepository(context: Context): SessionRepository {
        val pref = SessionPreference.getInstance(context.dataStore)
        val api = ApiConfig.getApiService()
        val session = runBlocking { pref.getSession().first() }
        val apitoken = ApiConfig.getApiToken(session.token)
        return SessionRepository.getInstance(pref,api,apitoken)
    }
}