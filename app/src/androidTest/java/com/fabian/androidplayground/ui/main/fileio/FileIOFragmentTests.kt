package com.fabian.androidplayground.ui.main.fileio

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FileIOFragmentTests {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.fabian.androidplayground", appContext.packageName)
    }

    @Test
    fun getDrawableFromUri() {

    }

    @Test
    fun getDrawableFromUriPlaceholderCase() {

    }
}