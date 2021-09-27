package com.fabian.androidplayground.ui.main.fileio

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fabian.androidplayground.common.utils.IntentUtils
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class IntentUtilsTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.fabian.androidplayground", appContext.packageName)
    }

    @Test
    fun getTextFromIntent() {
        val testString = "testString"

        val i = Intent(Intent.ACTION_SEND)
        i.putExtra(Intent.EXTRA_TEXT, testString)
        val getString = IntentUtils.getTextFromIntent(i)

        assertEquals(testString, getString)
    }

    @Test
    fun getFileFromIntent() {
        val testFile = File(InstrumentationRegistry.getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "testFile")
        if (!testFile.exists()) {
            testFile.createNewFile()
        }

        val toURI = testFile.toUri()

        val i = Intent(Intent.ACTION_SEND)
        i.putExtra(Intent.EXTRA_STREAM, toURI)
        val getUri = IntentUtils.getFileUriFromIntent(i)

        assertEquals(toURI, getUri!![0])
    }
}