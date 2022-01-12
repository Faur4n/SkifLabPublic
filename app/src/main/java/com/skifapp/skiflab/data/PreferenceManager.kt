package com.skifapp.skiflab.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val store = context.dataStore

    suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return get(key).firstOrNull()
    }

    suspend fun update(transform: (MutablePreferences) -> Unit) {
        store.edit(transform)
    }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return store.data.map { preferences ->
            preferences[key]
        }
    }


    companion object {
        val currentAccessKey = stringPreferencesKey("current_access_key")

    }
}