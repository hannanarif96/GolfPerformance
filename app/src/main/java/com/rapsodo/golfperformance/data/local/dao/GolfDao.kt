package com.rapsodo.golfperformance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rapsodo.golfperformance.data.local.entities.PlayerEntity
import com.rapsodo.golfperformance.data.local.entities.ShotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GolfDao {
    @Query("SELECT * FROM players")
    fun getPlayers(): Flow<List<PlayerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("SELECT * FROM shots WHERE playerId = :playerId")
    fun getShotsForPlayer(playerId: String): Flow<List<ShotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShots(shots: List<ShotEntity>)
}
