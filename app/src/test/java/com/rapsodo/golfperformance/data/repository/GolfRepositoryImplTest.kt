package com.rapsodo.golfperformance.data.repository

import com.rapsodo.golfperformance.data.local.dao.GolfDao
import com.rapsodo.golfperformance.data.remote.GolfApiService
import com.rapsodo.golfperformance.data.remote.dto.PlayerDto
import com.rapsodo.golfperformance.data.remote.dto.ShotDto
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class GolfRepositoryImplTest {

    private lateinit var api: GolfApiService
    private lateinit var dao: GolfDao
    private lateinit var repository: GolfRepositoryImpl

    @Before
    fun setup() {
        api = mock()
        dao = mock()
        repository = GolfRepositoryImpl(api, dao)
    }

    @Test
    fun `refreshPlayers fetches from api and inserts into dao`() = runTest {
        // Arrange
        val remotePlayers = listOf(
            PlayerDto("1", "Tiger", "img1", "Club1", 180.0, 300.0)
        )
        whenever(api.getPlayers()).thenReturn(remotePlayers)

        // Act
        repository.refreshPlayers()

        // Assert
        verify(api).getPlayers()
        verify(dao).insertPlayers(any())
    }

    @Test
    fun `refreshPlayerShots fetches from api and inserts into dao`() = runTest {
        // Arrange
        val playerId = "p1"
        val remoteShots = listOf(
            ShotDto("s1", playerId, 150.0, 10.0, 250.0, "Driver", 2000.0, 123L)
        )
        whenever(api.getPlayerShots(playerId, 1, 50)).thenReturn(remoteShots)

        // Act
        repository.refreshPlayerShots(playerId)

        // Assert
        verify(api).getPlayerShots(playerId, 1, 50)
        verify(dao).insertShots(any())
    }
}
