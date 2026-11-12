package com.example.seldonfleet.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seldonfleet.domain.repository.FleetRepository
import com.example.seldonfleet.ui.screens.detail.DetailScreen
import com.example.seldonfleet.ui.screens.detail.DetailViewModel
import com.example.seldonfleet.ui.screens.fleet.FleetScreen
import com.example.seldonfleet.ui.screens.fleet.FleetViewModel

@Composable
fun AppNavHost(repository: FleetRepository) {
    val fleetViewModel: FleetViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FleetViewModel(repository) as T
    })
    val detailViewModel: DetailViewModel = viewModel()
    val fleet by fleetViewModel.state.collectAsState()
    var selectedId by remember { mutableStateOf<String?>(null) }
    val selected = fleet.devices.firstOrNull { it.id == selectedId }
    BackHandler(enabled = selectedId != null) { selectedId = null }
    if (selected == null) FleetScreen(fleetViewModel) { selectedId = it.id }
    else DetailScreen(selected, detailViewModel, onBack = { selectedId = null }, onToggleFavorite = { fleetViewModel.toggleFavorite(selected) })
}
