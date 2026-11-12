package com.example.seldonfleet.domain.repository

import com.example.seldonfleet.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface FleetRepository {
    suspend fun getFleet(): List<Device>
    fun observeFavorites(): Flow<Set<String>>
    suspend fun toggleFavorite(id: String)
}
