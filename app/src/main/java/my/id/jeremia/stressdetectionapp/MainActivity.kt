package my.id.jeremia.stressdetectionapp

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    var currentPage = 0;
    var NUM_PAGES=0;
    val timer = Timer();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.editTextUsername)
        val programstudi = findViewById<EditText>(R.id.editTextProgramStudi)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        viewPager.apply {
            clipChildren = true  // No clipping the left and right items
            clipToPadding = true  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        val imgData = arrayListOf(
            "@drawable/stress1",
            "@drawable/stress2",
            "@drawable/stress3",
            "@drawable/stress4",
            "@drawable/stress5",
        )
        NUM_PAGES = imgData.size

        viewPager.adapter = CarouselRVAdapter(imgData,baseContext)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }

        val startBtn = findViewById<Button>(R.id.mainStartBtn)
        startBtn.setOnClickListener {
            startActivity(Intent(this, SetupCamera::class.java).apply {
                this.putExtra("username", username.text.toString())
                this.putExtra("programstudi", programstudi.text.toString())
            })
        }

        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES - 1) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
        }

        timer.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, 500, 2500)

    }
}