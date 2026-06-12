package com.rapsodo.golfperformance.ui.playerdetail

import app.cash.turbine.test
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: GolfRepository
    private lateinit var viewModel: PlayerDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = PlayerDetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadShots calls repository paging data and triggers refresh`() = runTest {
        // Arrange
        val playerId = "p1"
        whenever(repository.getPlayerShotsPaging(playerId)).thenReturn(flowOf())

        // Act
        viewModel.loadShots(playerId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        verify(repository).getPlayerShotsPaging(playerId)
        verify(repository).refreshPlayerShots(playerId)
    }

    @Test
    fun `refreshShots updates loading state`() = runTest {
        // Arrange
        val playerId = "p1"

        // Act & Assert
        viewModel.isLoading.test {
            assertEquals(false, awaitItem()) // Initial state
            
            viewModel.refreshShots(playerId)
            assertEquals(true, awaitItem())
            
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(false, awaitItem())
        }
    }
}
