package com.rapsodo.golfperformance.domain.model

data class Player(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val clubName: String,
    val averageBallSpeed: Double,
    val averageCarryDistance: Double
)
