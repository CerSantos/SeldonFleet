package com.example.seldonfleet.data.model

data class FleetDto(val devices: List<DeviceDto>)

/** Nullable values mirror the imperfect input file without inventing defaults. */
data class DeviceDto(
    val id: String?, val model: String?, val soc: String?, val tier: String?,
    val ramMb: Int?, val androidApi: Int?, val batteryCapacityMah: Double?,
    val batteryLevelPct: Double?, val screenOnMinutes: Double?,
    val energyConsumed: Double?, val energyUnit: String?, val nominalVoltage: Double?,
    val jankFramesPct: Double?, val crashCount7d: Int?, val lastSeen: String?
)
