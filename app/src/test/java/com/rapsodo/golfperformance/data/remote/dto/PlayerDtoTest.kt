package com.rapsodo.golfperformance.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerDtoTest {

    @Test
    fun `toDomain converts PlayerDto to Player domain model correctly`() {
        // Arrange
        val dto = PlayerDto(
            id = "p1",
            name = "Tiger Woods",
            profileImageUrl = "https://example.com/tiger.jpg",
            clubName = "Jupiter Island Club",
            averageBallSpeed = 180.2,
            averageCarryDistance = 305.5
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertEquals(dto.id, domain.id)
        assertEquals(dto.name, domain.name)
        assertEquals(dto.profileImageUrl, domain.profileImageUrl)
        assertEquals(dto.clubName, domain.clubName)
        assertEquals(dto.averageBallSpeed, domain.averageBallSpeed, 0.0)
        assertEquals(dto.averageCarryDistance, domain.averageCarryDistance, 0.0)
    }
}
