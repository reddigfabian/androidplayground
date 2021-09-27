package com.fabian.androidplayground.ui.share.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fabian.androidplayground.R
import com.fabian.androidplayground.ui.common.views.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

private const val TAG = "ShareActivity"

@ExperimentalCoroutinesApi
@FlowPreview
class ShareActivity : AppCompatActivity(R.layout.activity_share) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
    }
}