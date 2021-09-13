package com.fabian.androidplayground.db.common

import androidx.room.*
import com.fabian.androidplayground.api.picsum.Picsum

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)
    @Query("SELECT * FROM remotekeys WHERE remoteKeyID = :id")
    suspend fun remoteKeysPicsumID(id: String): RemoteKeys?
    @Query("DELETE FROM remotekeys WHERE remoteKeyID = :id")
    fun delete(id: String)
    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}