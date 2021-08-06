package com.fabian.androidplayground.db.common

import androidx.room.*
import com.fabian.androidplayground.api.picsum.Picsum

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remotekeys WHERE repoIndex = :index")
    suspend fun remoteKeysPicsumID(index: Int): RemoteKeys?
    @Query("DELETE FROM remotekeys WHERE repoIndex = :index")
    fun delete(index: Int)
    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()
}