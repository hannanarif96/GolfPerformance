package com.rapsodo.golfperformance.data.remote.dto

import com.rapsodo.golfperformance.domain.model.Shot
import com.squareup.moshi.Json

data class ShotDto(
    @Json(name = "id") val id: String,
    @Json(name = "playerId") val playerId: String,
    @Json(name = "ballSpeed") val ballSpeed: Double,
    @Json(name = "launchAngle") val launchAngle: Double,
    @Json(name = "carryDistance") val carryDistance: Double,
    @Json(name = "clubType") val clubType: String,
    @Json(name = "spinRate") val spinRate: Double,
    @Json(name = "timestamp") val timestamp: Long
) {
    fun toDomain(): Shot {
        return Shot(
            id = id,
            playerId = playerId,
            ballSpeed = ballSpeed,
            launchAngle = launchAngle,
            carryDistance = carryDistance,
            clubType = clubType,
            spinRate = spinRate,
            timestamp = timestamp
        )
    }
}
