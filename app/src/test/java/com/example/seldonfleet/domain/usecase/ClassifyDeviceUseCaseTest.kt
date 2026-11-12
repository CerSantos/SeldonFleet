package com.example.seldonfleet.domain.usecase

import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.model.DeviceStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassifyDeviceUseCaseTest {
    private val classify = ClassifyDeviceUseCase()
    private fun device(relativeDrain: Double, crashes: Int = 0, jank: Double = 0.0, capacity: Double = 5000.0, minutes: Double = 60.0) = Device(
        id = "test", model = "test", soc = "test", tier = "mid", ramMb = null, androidApi = null,
        batteryCapacityMah = capacity, batteryLevelPct = 50.0, screenOnMinutes = minutes,
        energyConsumedMah = relativeDrain * capacity / 100.0, jankFramesPct = jank,
        crashCount7d = crashes, lastSeen = null
    )

    @Test fun zeroCapacityIsIndeterminate() { assertEquals(DeviceStatus.INDETERMINADO, classify(device(5.0, capacity = 0.0)).status) }
    @Test fun zeroScreenTimeIsIndeterminate() { assertEquals(DeviceStatus.INDETERMINADO, classify(device(5.0, minutes = 0.0)).status) }
    @Test fun criticalDrainThresholdAndBelow() {
        assertEquals(DeviceStatus.ATENCAO, classify(device(11.99)).status)
        assertEquals(DeviceStatus.CRITICO, classify(device(12.0)).status)
    }
    @Test fun attentionDrainThresholdAndBelow() {
        assertEquals(DeviceStatus.OK, classify(device(6.99)).status)
        assertEquals(DeviceStatus.ATENCAO, classify(device(7.0)).status)
    }
    @Test fun crashThresholdsAndBelow() {
        assertEquals(DeviceStatus.OK, classify(device(1.0, crashes = 0)).status)
        assertEquals(DeviceStatus.ATENCAO, classify(device(1.0, crashes = 1)).status)
        assertEquals(DeviceStatus.ATENCAO, classify(device(1.0, crashes = 2)).status)
        assertEquals(DeviceStatus.CRITICO, classify(device(1.0, crashes = 3)).status)
    }
    @Test fun jankThresholdsAndBelow() {
        assertEquals(DeviceStatus.OK, classify(device(1.0, jank = 7.99)).status)
        assertEquals(DeviceStatus.ATENCAO, classify(device(1.0, jank = 8.0)).status)
        assertEquals(DeviceStatus.ATENCAO, classify(device(1.0, jank = 14.99)).status)
        assertEquals(DeviceStatus.CRITICO, classify(device(1.0, jank = 15.0)).status)
    }
}
