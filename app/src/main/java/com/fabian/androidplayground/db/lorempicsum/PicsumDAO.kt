package com.fabian.androidplayground.db.lorempicsum

import androidx.paging.PagingSource
import androidx.room.*
import com.fabian.androidplayground.api.picsum.Picsum

@Dao
interface PicsumDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(picsum : Picsum)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Picsum>)
    @Delete
    fun delete(picsum : Picsum)
    @Query("SELECT * FROM picsum")
    fun pagingSource(): PagingSource<Int, Picsum>
    @Query("DELETE FROM picsum")
    suspend fun clearAll()
}