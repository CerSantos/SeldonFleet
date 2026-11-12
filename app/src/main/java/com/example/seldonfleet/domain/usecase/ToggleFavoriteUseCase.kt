package com.example.seldonfleet.domain.usecase

import com.example.seldonfleet.domain.repository.FleetRepository

class ToggleFavoriteUseCase(private val repository: FleetRepository) {
    suspend operator fun invoke(id: String) = repository.toggleFavorite(id)
}
