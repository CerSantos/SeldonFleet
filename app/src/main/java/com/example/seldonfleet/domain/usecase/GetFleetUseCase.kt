package com.example.seldonfleet.domain.usecase

import com.example.seldonfleet.domain.repository.FleetRepository

class GetFleetUseCase(private val repository: FleetRepository) {
    suspend operator fun invoke() = repository.getFleet()
}
