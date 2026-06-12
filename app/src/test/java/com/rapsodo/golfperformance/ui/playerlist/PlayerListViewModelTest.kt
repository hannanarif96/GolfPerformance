package com.rapsodo.golfperformance.ui.playerlist

import app.cash.turbine.test
import com.rapsodo.golfperformance.domain.model.Player
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: GolfRepository
    private lateinit var viewModel: PlayerListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization triggers observePlayers and refresh`() = runTest {
        val players = listOf(
            Player("1", "Tiger", "img1", "Club1", 180.0, 300.0),
            Player("2", "Rory", "img2", "Club2", 182.0, 310.0)
        )

        whenever(repository.getPlayers()).thenReturn(flowOf(players))
        whenever(repository.refreshPlayers()).thenReturn(Unit)

        viewModel = PlayerListViewModel(repository)
        advanceUntilIdle()

        verify(repository).getPlayers()
        verify(repository).refreshPlayers()

        viewModel.players.test {
            // IMPORTANT: first emission in your setup is ALWAYS emptyList due to stateIn(Lazily)
            assertEquals(emptyList<Player>(), awaitItem())

            // second emission = real data
            assertEquals(players, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search query filters player list correctly`() = runTest {
        val players = listOf(
            Player("1", "Tiger Woods", "img1", "Jupiter", 180.0, 300.0),
            Player("2", "Rory McIlroy", "img2", "Bears", 182.0, 310.0)
        )

        whenever(repository.getPlayers()).thenReturn(flowOf(players))
        whenever(repository.refreshPlayers()).thenReturn(Unit)

        viewModel = PlayerListViewModel(repository)
        advanceUntilIdle()

        viewModel.players.test {
            // initial empty emission (stateIn lazy start behavior)
            assertEquals(emptyList<Player>(), awaitItem())

            // full list after repository emits
            assertEquals(players, awaitItem())

            // FILTER 1
            viewModel.onSearchQueryChanged("Tiger")
            advanceUntilIdle()

            assertEquals(
                listOf(Player("1", "Tiger Woods", "img1", "Jupiter", 180.0, 300.0)),
                awaitItem()
            )

            // FILTER 2
            viewModel.onSearchQueryChanged("Bears")
            advanceUntilIdle()

            assertEquals(
                listOf(Player("2", "Rory McIlroy", "img2", "Bears", 182.0, 310.0)),
                awaitItem()
            )

            // RESET
            viewModel.onSearchQueryChanged("")
            advanceUntilIdle()

            assertEquals(players, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh updates loading state`() = runTest {
        whenever(repository.getPlayers()).thenReturn(flowOf(emptyList()))
        whenever(repository.refreshPlayers()).thenReturn(Unit)

        viewModel = PlayerListViewModel(repository)

        viewModel.isLoading.test {
            // initial state
            assertEquals(false, awaitItem())

            viewModel.refresh()

            // loading ON
            assertEquals(true, awaitItem())

            advanceUntilIdle()

            // loading OFF
            assertEquals(false, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
