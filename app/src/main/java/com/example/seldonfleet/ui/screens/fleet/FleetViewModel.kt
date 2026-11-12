package com.example.seldonfleet.ui.screens.fleet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus
import com.example.seldonfleet.domain.repository.FleetRepository
import com.example.seldonfleet.domain.usecase.ClassifyDeviceUseCase
import com.example.seldonfleet.domain.usecase.GetFleetUseCase
import com.example.seldonfleet.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class FleetFilter { TODOS, FAVORITOS, OK, ATENCAO, CRITICO, INDETERMINADO }
data class FleetUiState(val devices: List<Device> = emptyList(), val filter: FleetFilter = FleetFilter.TODOS, val loading: Boolean = true, val error: String? = null)

class FleetViewModel(repository: FleetRepository) : ViewModel() {
    private val getFleet = GetFleetUseCase(repository)
    private val toggleFavorite = ToggleFavoriteUseCase(repository)
    private val classify = ClassifyDeviceUseCase()
    private val _state = MutableStateFlow(FleetUiState())
    val state: StateFlow<FleetUiState> = _state.asStateFlow()
    init {
        viewModelScope.launch {
            runCatching { getFleet() }
                .onSuccess { _state.value = _state.value.copy(devices = it, loading = false) }
                .onFailure { _state.value = _state.value.copy(loading = false, error = it.message ?: "Falha ao ler a frota") }
            repository.observeFavorites().collect { favorites ->
                _state.value = _state.value.copy(devices = _state.value.devices.map { it.copy(isFavorite = it.id in favorites) })
            }
        }
    }
    fun setFilter(filter: FleetFilter) { _state.value = _state.value.copy(filter = filter) }
    fun toggleFavorite(device: Device) { viewModelScope.launch { toggleFavorite(device.id) } }
    fun status(device: Device): DeviceStatus = classify(device).status
    fun filtered(state: FleetUiState): List<Device> = state.devices.filter { device ->
        when (state.filter) {
            FleetFilter.TODOS -> true
            FleetFilter.FAVORITOS -> device.isFavorite
            FleetFilter.OK -> status(device) == DeviceStatus.OK
            FleetFilter.ATENCAO -> status(device) == DeviceStatus.ATENCAO
            FleetFilter.CRITICO -> status(device) == DeviceStatus.CRITICO
            FleetFilter.INDETERMINADO -> status(device) == DeviceStatus.INDETERMINADO
        }
    }
}
