package my.id.jeremia.stressdetectionapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import my.id.jeremia.stressdetectionapp.databinding.ActivitySetupCameraBinding
import my.id.jeremia.stressdetectionapp.utils.camera.Analyzer
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class SetupCamera : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySetupCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private val setupCameraViewModel: SetupCameraViewModel by viewModels()

    private lateinit var SESSIONID: String

    override fun onCreate(savedInstanceState: Bundle?) {

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")
        SESSIONID = intent.getStringExtra("sessionID") ?: ""

        super.onCreate(savedInstanceState)
        viewBinding = ActivitySetupCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        viewBinding.nextButton.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java).apply {
                this.putExtra("username", username)
                this.putExtra("programstudi", programstudi)
                this.putExtra("sessionID", SESSIONID)
            })
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }


    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                    it.setAnalyzer(cameraExecutor, Analyzer { img ->

                        val timage = TensorImage(DataType.FLOAT32)
                        timage.load(img)

                        val processedImage =
                            setupCameraViewModel.tensorflowRepository.processImage(timage)

                        val output =
                            setupCameraViewModel.tensorflowRepository.startInference(processedImage)
                        Log.e("RESULT", output.contentDeepToString())

                        runOnUiThread {
                            viewBinding.nextButton.isEnabled = true

                            viewBinding.smileProbTextView.text =
                                "Marah (${String.format("%.3f", output[0][0])}) "+
                                "Senyum (${String.format("%.3f", output[0][1])}) "+
                                "Sedih (${String.format("%.3f", output[0][3])})"
                        }

                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle Permission granted/rejected
        var permissionGranted = true
        permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && it.value == false) permissionGranted = false
        }
        if (!permissionGranted) {
            Toast.makeText(
                baseContext, "Permission request denied", Toast.LENGTH_SHORT
            ).show()
        } else {
            startCamera()
        }
    }

}


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_setup_camera)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}