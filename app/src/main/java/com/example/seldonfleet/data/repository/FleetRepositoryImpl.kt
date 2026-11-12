package com.example.seldonfleet.data.repository

import com.example.seldonfleet.data.local.AssetFileReader
import com.example.seldonfleet.data.local.FavoritesDataStore
import com.example.seldonfleet.data.mapper.DeviceMapper
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.repository.FleetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FleetRepositoryImpl(private val reader: AssetFileReader, private val favorites: FavoritesDataStore) : FleetRepository {
    override suspend fun getFleet(): List<Device> {
        val saved = favorites.favorites.first()
        return reader.readFleet().devices.map { dto -> DeviceMapper.toDomain(dto, dto.id in saved) }
    }
    override fun observeFavorites(): Flow<Set<String>> = favorites.favorites
    override suspend fun toggleFavorite(id: String) = favorites.toggle(id)
}
