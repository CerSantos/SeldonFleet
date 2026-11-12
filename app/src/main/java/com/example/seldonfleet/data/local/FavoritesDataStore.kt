package com.example.seldonfleet.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.favoritesStore by preferencesDataStore(name = "fleet_favorites")

class FavoritesDataStore(private val context: Context) {
    private val key = stringSetPreferencesKey("device_ids")
    val favorites: Flow<Set<String>> = context.favoritesStore.data.map { it[key] ?: emptySet() }
    suspend fun toggle(id: String) {
        context.favoritesStore.edit { prefs ->
            val current = prefs[key].orEmpty().toMutableSet()
            if (!current.add(id)) current.remove(id)
            prefs[key] = current
        }
    }
}
