package com.example.seldonfleet.data.local

import android.content.Context
import com.example.seldonfleet.data.model.DeviceDto
import com.example.seldonfleet.data.model.FleetDto
import org.json.JSONObject

class AssetFileReader(private val context: Context) {
    fun readFleet(): FleetDto {
        val json = context.assets.open("frota.json").bufferedReader().use { it.readText() }
        val devices = JSONObject(json).getJSONArray("devices")
        return FleetDto((0 until devices.length()).map { index ->
            val obj = devices.getJSONObject(index)
            fun number(name: String) = if (obj.isNull(name) || !obj.has(name)) null else obj.getDouble(name)
            fun int(name: String) = if (obj.isNull(name) || !obj.has(name)) null else obj.getInt(name)
            fun string(name: String) = if (obj.isNull(name) || !obj.has(name)) null else obj.getString(name)
            DeviceDto(string("id"), string("model"), string("soc"), string("tier"), int("ramMb"), int("androidApi"),
                number("batteryCapacityMah"), number("batteryLevelPct"), number("screenOnMinutes"), number("energyConsumed"),
                string("energyUnit"), number("nominalVoltage"), number("jankFramesPct"), int("crashCount7d"), string("lastSeen"))
        })
    }
}
