package com.fabian.androidplayground.common.navigation

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IntentNavArgs(val action : String?,
                         val type : String?,
                         val extras : Bundle?) : Parcelable {

    companion object {
        const val PARCEL_KEY = "intentArgs"

        fun fromIntent(intent : Intent) : IntentNavArgs {
            return IntentNavArgs(intent.action, intent.type, intent.extras)
        }

//        fun empty() : IntentNavArgs {
//            return IntentNavArgs(null, null, null)
//        }
    }

//    fun isEmpty() : Boolean {
//        return action == null && type == null && (extras == null || extras.isEmpty)
//    }

}