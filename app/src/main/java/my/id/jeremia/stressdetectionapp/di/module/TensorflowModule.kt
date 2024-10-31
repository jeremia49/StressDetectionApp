package my.id.jeremia.stressdetectionapp.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.id.jeremia.stressdetectionapp.utils.tensorflow.TensorflowUtils
import my.id.jeremia.stressdetectionapp.utils.tensorflow.operators.RescaleOp
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import java.nio.ByteBuffer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TensorflowModule {

    @Provides
    @Singleton
    fun provideTensorflowFile(
        @ApplicationContext context: Context
    ):ByteBuffer = TensorflowUtils.loadModelFile(
        context,
        "hedv1.tflite"
    )


    @Provides
    @Singleton
    fun provideTensorflow(file: ByteBuffer): Interpreter {
        return Interpreter(file)
    }

    @Provides
    @Singleton
    fun provideImageProcessor(): ImageProcessor {
        return ImageProcessor.Builder()
                .add(
                    ResizeWithCropOrPadOp(128, 128),
                )
                .add(
                    RescaleOp(),
                )
                .build()
    }

}