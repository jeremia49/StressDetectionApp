package my.id.jeremia.stressdetectionapp.Analyzer

import android.os.SystemClock
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

class FaceAnalyzer(val imageAnalyzer : (ImageProxy) -> Unit) : ImageAnalysis.Analyzer {
    private val ANALYSIS_DELAY_MS = 1000
    private val INVALID_TIME = -1L
    private var lastAnalysisTime = INVALID_TIME

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image ?: return

        val now = SystemClock.uptimeMillis()
        if (lastAnalysisTime != INVALID_TIME && (now - lastAnalysisTime < ANALYSIS_DELAY_MS)) {
            image.close();
            return
        }

        imageAnalyzer(image)

        lastAnalysisTime = now;
    }

}