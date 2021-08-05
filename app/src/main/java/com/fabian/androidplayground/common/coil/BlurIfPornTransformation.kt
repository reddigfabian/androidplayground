package com.fabian.androidplayground.common.coil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.applyCanvas
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

/**
 * A [Transformation] that applies a Gaussian blur to an image.
 *
 * @param context The [Context] used to create a [RenderScript] instance.
 * @param radius The radius of the blur.
 * @param sampling The sampling multiplier used to scale the image. Values > 1
 *  will downscale the image. Values between 0 and 1 will upscale the image.
 */

private const val TAG = "BlurIfPornTransformati"

@RequiresApi(18)
class BlurIfPornTransformation @JvmOverloads constructor(
    private val context: Context,
    private val radius: Float = DEFAULT_RADIUS,
    private val sampling: Float = DEFAULT_SAMPLING
) : Transformation {
    private val options = ImageClassifier.ImageClassifierOptions.builder().setMaxResults(1).build()
    private var imageClassifier : ImageClassifier? = null

    init {
        require(radius in 0.0..25.0) { "radius must be in [0, 25]." }
        require(sampling > 0) { "sampling must be > 0." }
    }

    override fun key(): String = "${BlurIfPornTransformation::class.java.name}-$radius-$sampling"

    private fun isPorn(bitmap: Bitmap) : Boolean {
        try {
            if (imageClassifier == null) {
                imageClassifier = ImageClassifier.createFromFileAndOptions(context, "nsfw_model.tflite", options)
            }
            val toString = imageClassifier?.classify(TensorImage.fromBitmap(bitmap))
                .toString()
            return toString.contains("porn") || toString.contains("hentai")
        } catch (ex : Exception) {
            Log.e(TAG, "Exception: ",ex)
        }
        return true
    }

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        if (isPorn(input)) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

            val scaledWidth = (input.width / sampling).toInt()
            val scaledHeight = (input.height / sampling).toInt()
            val output = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
            output.applyCanvas {
                scale(1 / sampling, 1 / sampling)
                drawBitmap(input, 0f, 0f, paint)
            }

            var script: RenderScript? = null
            var tmpInt: Allocation? = null
            var tmpOut: Allocation? = null
            var blur: ScriptIntrinsicBlur? = null
            try {
                script = RenderScript.create(context)
                tmpInt = Allocation.createFromBitmap(script, output, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT)
                tmpOut = Allocation.createTyped(script, tmpInt.type)
                blur = ScriptIntrinsicBlur.create(script, Element.U8_4(script))
                blur.setInput(tmpInt)
                blur.forEach(tmpOut)
                tmpOut.copyTo(output)
            } finally {
                script?.destroy()
                tmpInt?.destroy()
                tmpOut?.destroy()
                blur?.destroy()
            }

            return output
        } else {
            return input
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is BlurIfPornTransformation &&
                context == other.context &&
                radius == other.radius &&
                sampling == other.sampling
    }

    override fun hashCode(): Int {
        var result = context.hashCode()
        result = 32 * result + radius.hashCode()
        result = 32 * result + sampling.hashCode()
        return result
    }

    override fun toString(): String {
        return "BlurIfPornTransformation(context=$context, radius=$radius, sampling=$sampling)"
    }

    private companion object {
        private const val DEFAULT_RADIUS = 10f
        private const val DEFAULT_SAMPLING = 1f
    }
}