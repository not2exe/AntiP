package com.gtime.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gtime.Constants

@Dao
interface AppTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appDataBaseEntity: AppDataBaseEntity)

    @Query("SELECT * FROM ${Constants.APP_TABLE}")
    suspend fun getAll(): List<AppDataBaseEntity>

    @Query("SELECT * FROM ${Constants.APP_TABLE} WHERE packageName = :packageName")
    suspend fun getByName(packageName: String): AppDataBaseEntity?
}