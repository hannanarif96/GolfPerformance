package com.rapsodo.golfperformance.domain.model

data class Shot(
    val id: String,
    val playerId: String,
    val ballSpeed: Double,
    val launchAngle: Double,
    val carryDistance: Double,
    val clubType: String,
    val spinRate: Double,
    val timestamp: Long
)
