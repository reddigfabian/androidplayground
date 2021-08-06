package com.fabian.androidplayground.db.common

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(@PrimaryKey val repoIndex: String, val prevKey: Int?, val nextKey: Int?)