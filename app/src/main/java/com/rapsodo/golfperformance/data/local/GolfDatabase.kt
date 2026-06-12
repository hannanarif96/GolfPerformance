package com.rapsodo.golfperformance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rapsodo.golfperformance.data.local.dao.GolfDao
import com.rapsodo.golfperformance.data.local.entities.PlayerEntity
import com.rapsodo.golfperformance.data.local.entities.ShotEntity

@Database(entities = [PlayerEntity::class, ShotEntity::class], version = 1)
abstract class GolfDatabase : RoomDatabase() {
    abstract val dao: GolfDao
}
