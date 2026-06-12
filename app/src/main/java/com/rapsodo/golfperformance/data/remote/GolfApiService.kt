package com.rapsodo.golfperformance.data.remote

import com.rapsodo.golfperformance.data.remote.dto.PlayerDto
import com.rapsodo.golfperformance.data.remote.dto.ShotDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GolfApiService {
    @GET("api.php?endpoint=players")
    suspend fun getPlayers(): List<PlayerDto>

    @GET("api.php?endpoint=shots")
    suspend fun getPlayerShots(
        @Query("playerId") playerId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): List<ShotDto>
}
