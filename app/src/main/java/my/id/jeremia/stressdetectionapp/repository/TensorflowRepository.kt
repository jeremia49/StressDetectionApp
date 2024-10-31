package my.id.jeremia.stressdetectionapp.repository

import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import javax.inject.Inject

class TensorflowRepository @Inject constructor(
    private val interpreter: Interpreter,
    private val imageProcessor: ImageProcessor,
){

    companion object{
        val OUTPUT_SIZE = 4
    }

    fun startInference(inputData: TensorImage): Array<FloatArray> {
            val input = inputData.tensorBuffer.buffer
            val output = Array(1) { FloatArray(OUTPUT_SIZE) }
            interpreter.run(input, output)
            return output
    }

    fun processImage(image: TensorImage):TensorImage = imageProcessor.process(image)

}