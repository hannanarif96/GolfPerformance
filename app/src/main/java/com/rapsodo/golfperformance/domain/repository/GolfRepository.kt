package com.rapsodo.golfperformance.domain.repository

import com.rapsodo.golfperformance.domain.model.Player
import com.rapsodo.golfperformance.domain.model.Shot
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GolfRepository {
    fun getPlayers(): Flow<List<Player>>
    suspend fun refreshPlayers()
    fun getPlayerShots(playerId: String): Flow<List<Shot>>
    fun getPlayerShotsPaging(playerId: String): Flow<PagingData<Shot>>
    suspend fun refreshPlayerShots(playerId: String)
}
