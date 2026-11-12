package com.example.seldonfleet.data.mapper

import com.example.seldonfleet.data.model.DeviceDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeviceMapperTest {
    private fun dto(consumed: Double, unit: String, voltage: Double?) = DeviceDto(
        id = "D1", model = "Model", soc = "SoC", tier = "mid", ramMb = 4096, androidApi = 34,
        batteryCapacityMah = 5000.0, batteryLevelPct = 50.0, screenOnMinutes = 60.0,
        energyConsumed = consumed, energyUnit = unit, nominalVoltage = voltage,
        jankFramesPct = 0.0, crashCount7d = 0, lastSeen = null
    )

    @Test fun convertsMilliwattHoursToMilliampHours() {
        assertEquals(980.0, DeviceMapper.toDomain(dto(3773.0, "mWh", 3.85)).energyConsumedMah!!, 0.1)
    }

    @Test fun keepsMilliampHoursUnchanged() {
        assertEquals(980.0, DeviceMapper.toDomain(dto(980.0, "mAh", null)).energyConsumedMah!!, 0.0)
    }

    @Test fun missingVoltageMakesMilliwattHoursUnavailable() {
        assertNull(DeviceMapper.toDomain(dto(3773.0, "mWh", null)).energyConsumedMah)
    }
}
