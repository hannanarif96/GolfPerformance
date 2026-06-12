package com.rapsodo.golfperformance.data.paging

import androidx.paging.PagingSource
import com.rapsodo.golfperformance.data.remote.GolfApiService
import com.rapsodo.golfperformance.data.remote.dto.ShotDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ShotPagingSourceTest {

    private lateinit var api: GolfApiService
    private lateinit var pagingSource: ShotPagingSource
    private val playerId = "p1"

    @Before
    fun setup() {
        api = mock()
        pagingSource = ShotPagingSource(api, playerId)
    }

    @Test
    fun `load returns success on valid data`() = runTest {
        // Arrange
        val remoteShots = listOf(
            ShotDto("s1", playerId, 150.0, 10.0, 250.0, "Driver", 2000.0, 123L)
        )
        whenever(api.getPlayerShots(playerId, 1, 20)).thenReturn(remoteShots)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Assert
        val expected = PagingSource.LoadResult.Page(
            data = remoteShots.map { it.toDomain() },
            prevKey = null,
            nextKey = 2
        )
        assertEquals(expected, result)
    }

    @Test
    fun `load returns error on api exception`() = runTest {
        // Arrange
        val exception = RuntimeException("API Error")
        whenever(api.getPlayerShots(playerId, 1, 20)).thenThrow(exception)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)
    }
}
