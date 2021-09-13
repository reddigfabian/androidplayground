package com.fabian.androidplayground.db.lorempicsum

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.db.common.RemoteKeys
import com.fabian.androidplayground.db.common.RemoteKeysDao

@Database(version = 1, entities = [Picsum::class, RemoteKeys::class], exportSchema = false)
abstract class LoremPicsumDatabase : RoomDatabase() {
    abstract fun getRemoteKeysDao(): RemoteKeysDao
    abstract fun getPicsumDao(): PicsumDAO

    companion object {
        @Volatile
        private var INSTANCE: LoremPicsumDatabase? = null

        fun getInstance(context: Context): LoremPicsumDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

//        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, LoremPicsumDatabase::class.java, "LoremPicsum").build()
        private fun buildDatabase(context: Context) = Room.inMemoryDatabaseBuilder(context.applicationContext, LoremPicsumDatabase::class.java).build()
    }

    override fun close() {
        INSTANCE = null
        super.close()
    }
}