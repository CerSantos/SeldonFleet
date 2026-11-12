package com.example.seldonfleet.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus
import com.example.seldonfleet.ui.screens.fleet.statusLabel

@Composable
fun DetailScreen(device: Device, viewModel: DetailViewModel, onBack: () -> Unit, onToggleFavorite: () -> Unit) {
    val result = viewModel.classification(device)
    val accent = when(result.status) {
        DeviceStatus.OK -> Color(0xFF3ECF8E); DeviceStatus.ATENCAO -> Color(0xFFF2B441)
        DeviceStatus.CRITICO -> Color(0xFFFF6B6B); DeviceStatus.INDETERMINADO -> Color(0xFF7A8AA0)
    }
    Column(Modifier.fillMaxSize().background(Color(0xFF070D16)).statusBarsPadding().verticalScroll(rememberScrollState()).padding(20.dp)) {
        TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) { Text("‹  Frota", color = Color(0xFF4D93FF)) }
        Spacer(Modifier.height(18.dp))
        Text("${device.id}  /  ${device.tier.uppercase()}", color = Color(0xFF4D93FF), style = MaterialTheme.typography.labelLarge)
        Text(device.model, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(device.soc, color = Color(0xFF9BB0C7))
        Spacer(Modifier.height(20.dp))
        Column(Modifier.fillMaxWidth().background(Color(0xFF101B2A), RoundedCornerShape(14.dp)).padding(18.dp)) {
            Text(statusLabel(result.status).uppercase(), color = accent, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(result.reason, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            result.drainPercentPerHour?.let { Text("Dreno relativo · ${"%.1f".format(it)} %/h", color = Color(0xFF9BB0C7), modifier = Modifier.padding(top = 10.dp)) }
        }
        Spacer(Modifier.height(18.dp))
        Text("MÉTRICAS DO DISPOSITIVO", color = Color(0xFF9BB0C7), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Metric("Consumo normalizado", result.drainMahPerHour?.let { "${"%.1f".format(it)} mAh/h" } ?: "Indisponível")
        Metric("Energia consumida", device.energyConsumedMah?.let { "${"%.1f".format(it)} mAh" } ?: "Indisponível")
        Metric("Capacidade", device.batteryCapacityMah?.let { "${it.toInt()} mAh" } ?: "Indisponível")
        Metric("Bateria atual", device.batteryLevelPct?.let { "${it.toInt()} %" } ?: "Indisponível")
        Metric("Tela ligada", device.screenOnMinutes?.let { "${it.toInt()} min" } ?: "Indisponível")
        Metric("Quadros perdidos", device.jankFramesPct?.let { "$it %" } ?: "Indisponível")
        Metric("Falhas (7d)", device.crashCount7d?.toString() ?: "Indisponível")
        Metric("Nível de API", device.androidApi?.toString() ?: "Indisponível")
        Metric("Último contato", device.lastSeen ?: "Indisponível")
        Spacer(Modifier.height(18.dp))
        Button(onClick = onToggleFavorite, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF17283C))) {
            Text(if (device.isFavorite) "★  Favorito · persistido" else "☆  Adicionar aos favoritos", color = Color.White)
        }
    }
}

@Composable
private fun Metric(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF9BB0C7)); Text(value, color = Color.White, fontWeight = FontWeight.Medium)
    }
    HorizontalDivider(color = Color(0xFF1E3047))
}
