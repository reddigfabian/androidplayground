package com.fabian.androidplayground.db.common

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(@PrimaryKey val remoteKeyID: String, val prevKey: Int?, val nextKey: Int?)