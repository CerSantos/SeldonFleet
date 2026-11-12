package com.example.seldonfleet.domain.usecase

import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus

data class Classification(val status: DeviceStatus, val reason: String, val drainMahPerHour: Double?, val drainPercentPerHour: Double?)

class ClassifyDeviceUseCase {
    operator fun invoke(device: Device): Classification {
        val capacity = device.batteryCapacityMah
        val minutes = device.screenOnMinutes
        val energy = device.energyConsumedMah
        if (capacity == null || capacity <= 0 || minutes == null || minutes <= 0 || energy == null || energy < 0 || !energy.isFinite()) {
            return Classification(DeviceStatus.INDETERMINADO, "Dados insuficientes para calcular o dreno com segurança.", null, null)
        }
        val absolute = energy / (minutes / 60.0)
        val relative = absolute / capacity * 100.0
        if (!absolute.isFinite() || !relative.isFinite()) return Classification(DeviceStatus.INDETERMINADO, "Dados insuficientes para calcular o dreno com segurança.", null, null)
        val jank = device.jankFramesPct ?: 0.0
        val crashes = device.crashCount7d ?: 0
        val result = when {
            relative >= 12.0 -> Classification(DeviceStatus.CRITICO, "Dreno relativo de ${f(relative)} %/h, acima do limiar crítico de 12 %/h.", absolute, relative)
            crashes >= 3 -> Classification(DeviceStatus.CRITICO, "$crashes falhas nos últimos 7 dias, acima do limiar crítico de 3.", absolute, relative)
            jank >= 15.0 -> Classification(DeviceStatus.CRITICO, "${f(jank)} % de quadros perdidos, acima do limiar crítico de 15 %.", absolute, relative)
            relative >= 7.0 -> Classification(DeviceStatus.ATENCAO, "Dreno relativo de ${f(relative)} %/h, acima do limiar de atenção de 7 %/h.", absolute, relative)
            crashes >= 1 -> Classification(DeviceStatus.ATENCAO, "$crashes falha(s) nos últimos 7 dias.", absolute, relative)
            jank >= 8.0 -> Classification(DeviceStatus.ATENCAO, "${f(jank)} % de quadros perdidos, acima do limiar de atenção de 8 %.", absolute, relative)
            else -> Classification(DeviceStatus.OK, "Nenhum sinal ultrapassa os limiares de atenção.", absolute, relative)
        }
        return result
    }

    private fun f(value: Double) = String.format(java.util.Locale("pt", "BR"), "%.1f", value)
}
