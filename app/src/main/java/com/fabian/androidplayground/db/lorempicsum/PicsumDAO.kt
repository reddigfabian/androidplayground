package com.fabian.androidplayground.db.lorempicsum

import androidx.paging.PagingSource
import androidx.room.*
import com.fabian.androidplayground.api.picsum.Picsum
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


@Dao
interface PicsumDAO {
    @Query("SELECT * FROM picsum WHERE id = :id")
    fun getPicsumFlow(id: String?): Flow<Picsum?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picsum : Picsum)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Picsum>)
    @Delete
    suspend fun delete(picsum : Picsum)
    @Query("SELECT * FROM picsum")
    fun pagingSource(): PagingSource<Int, Picsum>
    @Query("DELETE FROM picsum")
    suspend fun clearAll()
}