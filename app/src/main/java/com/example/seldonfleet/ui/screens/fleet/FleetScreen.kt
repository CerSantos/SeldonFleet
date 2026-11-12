package com.example.seldonfleet.ui.screens.fleet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus

private val canvas = Color(0xFF070D16)
private val panel = Color(0xFF101B2A)
private val muted = Color(0xFF9BB0C7)
private fun statusColor(status: DeviceStatus) = when(status) {
    DeviceStatus.OK -> Color(0xFF3ECF8E); DeviceStatus.ATENCAO -> Color(0xFFF2B441)
    DeviceStatus.CRITICO -> Color(0xFFFF6B6B); DeviceStatus.INDETERMINADO -> Color(0xFF7A8AA0)
}
fun statusLabel(status: DeviceStatus) = when(status) {
    DeviceStatus.OK -> "OK"; DeviceStatus.ATENCAO -> "Atenção"; DeviceStatus.CRITICO -> "Crítico"; DeviceStatus.INDETERMINADO -> "Sem dados"
}

@Composable
fun FleetScreen(viewModel: FleetViewModel, onDeviceClick: (Device) -> Unit) {
    val state by viewModel.state.collectAsState()
    val counts = state.devices.groupingBy(viewModel::status).eachCount()
    val filters = listOf(FleetFilter.TODOS, FleetFilter.FAVORITOS, FleetFilter.OK, FleetFilter.ATENCAO, FleetFilter.CRITICO, FleetFilter.INDETERMINADO)
    Column(Modifier.fillMaxSize().background(canvas).statusBarsPadding()) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            Text("SELDON / OPERAÇÕES", style = MaterialTheme.typography.labelMedium, color = Color(0xFF4D93FF), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Frota", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
            Text("${state.devices.size} dispositivos · dados locais", color = muted, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(DeviceStatus.OK, DeviceStatus.ATENCAO, DeviceStatus.CRITICO, DeviceStatus.INDETERMINADO).forEach { status ->
                    Column(Modifier.weight(1f).background(panel, RoundedCornerShape(10.dp)).padding(10.dp)) {
                        Text("${counts[status] ?: 0}", color = statusColor(status), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(statusLabel(status), color = muted, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                    }
                }
            }
        }
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Spacer(Modifier.width(12.dp))
            filters.forEach { filter ->
                val label = when(filter) { FleetFilter.TODOS -> "Todos"; FleetFilter.FAVORITOS -> "★ Favoritos"; else -> statusLabel(DeviceStatus.valueOf(filter.name)) }
                FilterChip(selected = state.filter == filter, onClick = { viewModel.setFilter(filter) }, label = { Text(label) })
            }
        }
        when {
            state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            state.error != null -> Text(state.error!!, Modifier.padding(24.dp), color = Color(0xFFFF6B6B))
            else -> LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(viewModel.filtered(state), key = { it.id }) { device ->
                    val status = viewModel.status(device)
                    Row(Modifier.fillMaxWidth().background(panel, RoundedCornerShape(12.dp)).clickable { onDeviceClick(device) }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(device.id, color = Color(0xFF4D93FF), style = MaterialTheme.typography.labelMedium)
                            Text(device.model, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text("${device.soc} · bateria ${device.batteryLevelPct?.let { "${it.toInt()}%" } ?: "—"}", color = muted, style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("● ${statusLabel(status)}", color = statusColor(status), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Text(if (device.isFavorite) "★" else "", color = Color(0xFFF2B441))
                        }
                    }
                }
            }
        }
    }
}
