package com.rapsodo.golfperformance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rapsodo.golfperformance.domain.model.Shot

@Entity(tableName = "shots")
data class ShotEntity(
    @PrimaryKey val id: String,
    val playerId: String,
    val ballSpeed: Double,
    val launchAngle: Double,
    val carryDistance: Double,
    val clubType: String,
    val spinRate: Double,
    val timestamp: Long
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

    companion object {
        fun fromDomain(shot: Shot): ShotEntity {
            return ShotEntity(
                id = shot.id,
                playerId = shot.playerId,
                ballSpeed = shot.ballSpeed,
                launchAngle = shot.launchAngle,
                carryDistance = shot.carryDistance,
                clubType = shot.clubType,
                spinRate = shot.spinRate,
                timestamp = shot.timestamp
            )
        }
    }
}
