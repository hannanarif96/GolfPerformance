package com.rapsodo.golfperformance.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class ShotDtoTest {

    @Test
    fun `toDomain converts DTO to Domain model correctly`() {
        // Arrange
        val dto = ShotDto(
            id = "1",
            playerId = "p1",
            ballSpeed = 150.0,
            launchAngle = 10.0,
            carryDistance = 250.0,
            clubType = "Driver",
            spinRate = 2000.0,
            timestamp = 123456789L
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals(dto.id, domain.id)
        assertEquals(dto.playerId, domain.playerId)
        assertEquals(dto.ballSpeed, domain.ballSpeed, 0.0)
        assertEquals(dto.launchAngle, domain.launchAngle, 0.0)
        assertEquals(dto.carryDistance, domain.carryDistance, 0.0)
        assertEquals(dto.clubType, domain.clubType)
        assertEquals(dto.spinRate, domain.spinRate, 0.0)
        assertEquals(dto.timestamp, domain.timestamp)
    }
}
