package com.rapsodo.golfperformance.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rapsodo.golfperformance.data.local.dao.GolfDao
import com.rapsodo.golfperformance.data.local.entities.PlayerEntity
import com.rapsodo.golfperformance.data.local.entities.ShotEntity
import com.rapsodo.golfperformance.data.paging.ShotPagingSource
import com.rapsodo.golfperformance.data.remote.GolfApiService
import com.rapsodo.golfperformance.domain.model.Player
import com.rapsodo.golfperformance.domain.model.Shot
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class GolfRepositoryImpl(
    private val api: GolfApiService,
    private val dao: GolfDao
) : GolfRepository {

    override fun getPlayers(): Flow<List<Player>> {
        return dao.getPlayers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshPlayers() {
        try {
            Timber.d("Refreshing players from remote")
            val remotePlayers = api.getPlayers()
            Timber.d("Successfully fetched ${remotePlayers.size} players")
            dao.insertPlayers(remotePlayers.map { PlayerEntity.fromDomain(it.toDomain()) })
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing players")
            throw e
        }
    }

    override fun getPlayerShots(playerId: String): Flow<List<Shot>> {
        return dao.getShotsForPlayer(playerId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getPlayerShotsPaging(playerId: String): Flow<PagingData<Shot>> {
        Timber.d("Getting paging data for player: $playerId")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ShotPagingSource(api, playerId) }
        ).flow
    }

    override suspend fun refreshPlayerShots(playerId: String) {
        try {
            Timber.d("Refreshing shots for player: $playerId")
            val remoteShots = api.getPlayerShots(playerId, 1, 50)
            Timber.d("Successfully fetched ${remoteShots.size} shots")
            dao.insertShots(remoteShots.map { ShotEntity.fromDomain(it.toDomain()) })
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing shots for player: $playerId")
            throw e
        }
    }
}
