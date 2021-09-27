package com.fabian.androidplayground.ui.main.fileio.list.viewmodels

import android.graphics.drawable.Drawable
import android.net.Uri
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem

class FileItemViewModel private constructor(val uri : Uri?, val type : String, val drawable : Drawable?, val text : String?) {

    constructor(uri : Uri, type : String, drawable : Drawable?) : this(uri, type, drawable, null)
    constructor(text : String) : this(null, "text", null, text)

    fun toRecyclerItem() =
        RecyclerItem(
            data = this,
            variableId = BR.fileItemViewModel,
            layoutID = R.layout.item_file_list
        )
}