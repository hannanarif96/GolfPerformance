package com.rapsodo.golfperformance.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rapsodo.golfperformance.data.remote.GolfApiService
import com.rapsodo.golfperformance.domain.model.Shot
import timber.log.Timber

class ShotPagingSource(
    private val api: GolfApiService,
    private val playerId: String
) : PagingSource<Int, Shot>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Shot> {
        val position = params.key ?: 1
        Timber.d("Paging: Loading page $position for player $playerId")
        return try {
            val response = api.getPlayerShots(playerId, position, params.loadSize)
            val shots = response.map { it.toDomain() }
            Timber.d("Paging: Successfully loaded ${shots.size} shots")
            LoadResult.Page(
                data = shots,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (shots.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Timber.e(exception, "Paging: Error loading page $position")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Shot>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
