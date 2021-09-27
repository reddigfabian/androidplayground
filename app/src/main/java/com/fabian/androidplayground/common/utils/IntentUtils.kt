package com.fabian.androidplayground.common.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle

object IntentUtils {
    fun getTextFromIntentBundle(bundle: Bundle) : String? {
        return bundle.getString(Intent.EXTRA_TEXT)
    }

    fun getFileUriFromIntentBundle(bundle: Bundle): List<Uri>? {
        val list = mutableListOf<Uri>()
        bundle.getParcelable<Uri>(Intent.EXTRA_STREAM)?.let { uri ->
            list.add(uri)
        }
        return if (list.size == 0) {
            null
        } else {
            list
        }
    }

    fun getMultipleFileUrisFromIntentBundle(bundle: Bundle) : List<Uri>? {
        val list = mutableListOf<Uri>()
        bundle.getParcelableArrayList<Uri>(Intent.EXTRA_STREAM)?.let { uris ->
            uris.forEach { parcelable ->
                parcelable?.let { uri ->
                    list.add(uri)
                }
            }
        }
        return if (list.size == 0) {
            null
        } else {
            list
        }
    }
}