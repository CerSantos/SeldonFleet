package com.example.seldonfleet.data.mapper

import com.example.seldonfleet.data.model.DeviceDto
import com.example.seldonfleet.domain.model.Device

object DeviceMapper {
    fun toDomain(dto: DeviceDto, favorite: Boolean = false): Device {
        val energyMah = when (dto.energyUnit?.lowercase()) {
            "mah" -> dto.energyConsumed
            "mwh" -> if ((dto.nominalVoltage ?: 0.0) > 0.0) dto.energyConsumed?.div(dto.nominalVoltage!!) else null
            else -> null
        }
        return Device(
            id = dto.id.orEmpty(), model = dto.model.orEmpty(), soc = dto.soc.orEmpty(), tier = dto.tier.orEmpty(),
            ramMb = dto.ramMb, androidApi = dto.androidApi, batteryCapacityMah = dto.batteryCapacityMah,
            batteryLevelPct = dto.batteryLevelPct, screenOnMinutes = dto.screenOnMinutes,
            energyConsumedMah = energyMah, jankFramesPct = dto.jankFramesPct,
            crashCount7d = dto.crashCount7d, lastSeen = dto.lastSeen, isFavorite = favorite
        )
    }
}
