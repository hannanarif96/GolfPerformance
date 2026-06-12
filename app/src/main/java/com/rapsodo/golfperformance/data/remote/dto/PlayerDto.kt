package com.rapsodo.golfperformance.data.remote.dto

import com.rapsodo.golfperformance.domain.model.Player
import com.squareup.moshi.Json

data class PlayerDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "profileImageUrl") val profileImageUrl: String,
    @Json(name = "clubName") val clubName: String,
    @Json(name = "averageBallSpeed") val averageBallSpeed: Double,
    @Json(name = "averageCarryDistance") val averageCarryDistance: Double
) {
    fun toDomain(): Player {
        return Player(
            id = id,
            name = name,
            profileImageUrl = profileImageUrl,
            clubName = clubName,
            averageBallSpeed = averageBallSpeed,
            averageCarryDistance = averageCarryDistance
        )
    }
}
