package my.id.jeremia.stressdetectionapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.Objects


class QuestionActivity : AppCompatActivity() {
    lateinit var ANSWERS : IntArray;
    val PERTANYAAAN : LinkedHashMap<String, Double> = linkedMapOf(
        "Sering Lupa/Pelupa" to 0.80,
        "Sulit Berkonsentrasi/Tidak Fokus" to 0.80,
        "Merasa Cemas" to 0.75,
        "Suasana Hari Mudah Berubah" to 0.75,
        "Nafsu Makan Menurun" to 0.65,
        "Mudah Tersinggung" to 0.75,
        "Sulit Tidur/Lebih Banyak Tidur" to 0.80,
        "Mudah Menangis" to 0.70,
        "Diare" to 0.80,
        "Maag" to 0.75,
        "Ketegangan Pada otot Tertentu, terutama bagian leher, bahu, dan punggung" to 0.75,
        "Sakit Kepala" to 0.70,
        "Emosi Berlebih dan Tidak Terkontrol" to 0.70,
        "Keringat Berlebih" to 0.55,
        "Kurang Semangat" to 0.70,
        "Hilang Rasa Percaya Diri" to 0.95,
        "Gatal – Gatal pada Kulit/alergi" to 0.95,
        "Gangguan Pencernaan Berat" to 0.55,
        "Jantung Bedebar Semakin Meningkat" to 0.80,
        "Sesak Nafas" to 0.50,
        "Tremor (Gemetar tidak Terkendali)" to 0.70,
        "Perasaan Cemas dan Takut Meningkat" to 0.70,
        "Pikiran Kacau" to 0.85,
        "Sensitif / Mudah Tersinggung" to 0.80,
        "Kehilangan Rasa Humor" to 0.80,
        "Prestasi Menurun" to 0.75,
        "Mudah Lelah" to 0.75,
        "Merasa Sudah Tidak ada Harapan/ Putus Asa" to 0.70,
        "Sakit Pinggang" to 0.55,
        "Terlalu Peka" to 0.70,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        ANSWERS = IntArray(PERTANYAAAN.size).apply{
            fill(-1,0)
        }

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")

        val questionHeaderText = findViewById<TextView>(R.id.questionHeaderText)
        questionHeaderText.text = "Nama : ${username}\nProgram Studi : ${programstudi}"

        for (i in 0..PERTANYAAAN.size-1) {
            val z = createItemPertanyaan(PERTANYAAAN.keys.elementAt(i), i)
            val insertPoint = findViewById<View>(R.id.containerPertanyaan) as ViewGroup
            insertPoint.addView(
                z,
                i
            )
        }

        findViewById<Button>(R.id.submitAnswer).setOnClickListener{

            var score = 0.0;
            for (i in 0..PERTANYAAAN.size-1) {
                if(ANSWERS[i] == -1){
                    Toast.makeText(baseContext,"Anda belum menjawab pertanyaan ${PERTANYAAAN.keys.elementAt(i)}",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                else if(ANSWERS[i] == 1 ){
                    score += PERTANYAAAN.values.elementAt(i)
                }
            }

            startActivity(Intent(this, ResultActivity::class.java).apply {

                this.putExtra("answers", ANSWERS)
                this.putExtra("username", username)
                this.putExtra("programstudi", programstudi)
                this.putExtra("score", score)

            })
        }


    }

    fun createItemPertanyaan(pertanyaan: String, idx: Int ) : View {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.item_pertanyaan, null)

        //Ganti Pertanyaan
        v.findViewById<TextView>(R.id.txtPertanyaan).text = pertanyaan
        v.findViewById<ChipGroup>(R.id.ansPertanyaan).setOnCheckedStateChangeListener{
                chipGroup: ChipGroup, ints: MutableList<Int> ->

//                Log.d("LOG", "ANSWER ${ANSWERS.contentToString()}")

                val chipid = chipGroup.checkedChipId
                if(chipid == View.NO_ID){
                    ANSWERS[idx] = -1
//                    v.findViewById<TextView>(R.id.txtScr).text = "NULL"
                    return@setOnCheckedStateChangeListener
                }
                val chip: Chip = chipGroup.findViewById(chipid)
                if(chip.text.toString().contentEquals("Ya")) {
                    ANSWERS[idx] = 1
//                    v.findViewById<TextView>(R.id.txtScr).text = "YA"
                    return@setOnCheckedStateChangeListener
                }else if(chip.text.toString().contentEquals("Tidak")) {
                    ANSWERS[idx] = 0
//                    v.findViewById<TextView>(R.id.txtScr).text = "TIDAK"
                    return@setOnCheckedStateChangeListener
                }
            return@setOnCheckedStateChangeListener
        }

        return v
    }


}