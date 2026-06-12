package com.rapsodo.golfperformance.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rapsodo.golfperformance.domain.model.Player

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val profileImageUrl: String,
    val clubName: String,
    val averageBallSpeed: Double,
    val averageCarryDistance: Double
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

    companion object {
        fun fromDomain(player: Player): PlayerEntity {
            return PlayerEntity(
                id = player.id,
                name = player.name,
                profileImageUrl = player.profileImageUrl,
                clubName = player.clubName,
                averageBallSpeed = player.averageBallSpeed,
                averageCarryDistance = player.averageCarryDistance
            )
        }
    }
}
