package com.example.seldonfleet.domain.model

data class Device(
    val id: String, val model: String, val soc: String, val tier: String,
    val ramMb: Int?, val androidApi: Int?, val batteryCapacityMah: Double?,
    val batteryLevelPct: Double?, val screenOnMinutes: Double?,
    val energyConsumedMah: Double?, val jankFramesPct: Double?,
    val crashCount7d: Int?, val lastSeen: String?, val isFavorite: Boolean = false
)
