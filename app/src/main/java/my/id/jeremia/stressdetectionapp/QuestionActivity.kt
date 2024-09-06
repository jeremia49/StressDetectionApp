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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionActivity : AppCompatActivity() {
    val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("penelitian")

    private lateinit var SESSIONID: String

    private val questionViewModel: QuestionViewModel by viewModels()

    lateinit var ANSWERS: FloatArray;
    val PERTANYAAAN: LinkedHashMap<String, Double> = linkedMapOf(
        "Apakah anda sering lupa atau pelupa?" to 0.80,
        "Apakah anda sulit berkonsentrasi atau tidak fokus?" to 0.80,
        "Apakah anda sering merasa cemas?" to 0.75,
        "Apakah anda suasana hari anda mudah berubah?" to 0.75,
        "Apakah anda nafsu makan anda sering menurun?" to 0.65,
        "Apakah anda sering mudah tersinggung?" to 0.75,
        "Apakah anda sering sulit tidur atau lebih banyak tidur?" to 0.80,
        "Apakah anda mudah menangis?" to 0.70,
        "Apakah anda sering diare?" to 0.80,
        "Apakah anda sering maag?" to 0.75,
        "Apakah anda sering ketegangan pada otot tertentu, terutama bagian leher, bahu, dan punggung?" to 0.75,
        "Apakah anda sering sakit kepala?" to 0.70,
        "Apakah anda sering merasa emosi berlebih dan tidak terkontrol?" to 0.70,
        "Apakah anda sering keringat berlebih?" to 0.55,
        "Apakah anda sering kurang semangat?" to 0.70,
        "Apakah anda sering hilang rasa percaya diri?" to 0.95,
        "Apakah anda sering gatal – gatal pada kulit/alergi?" to 0.95,
        "Apakah anda sering merasakan gangguan pencernaan berat?" to 0.55,
        "Apakah anda jantung bedebar anda semakin meningkat?" to 0.80,
        "Apakah anda sering merasa sesak nafas?" to 0.50,
        "Apakah anda sering tremor (gemetar tidak terkendali)?" to 0.70,
        "Apakah anda sering perasaan cemas dan takut meningkat?" to 0.70,
        "Apakah pikiran anda kacau?" to 0.85,
        "Apakah anda sering merasa sensitif / mudah tersinggung?" to 0.80,
        "Apakah anda kehilangan rasa humor?" to 0.80,
        "Apakah anda prestasi anda menurun?" to 0.75,
        "Apakah anda sering merasa mudah lelah?" to 0.75,
        "Apakah anda sering merasa sudah tidak ada harapan/ putus asa?" to 0.70,
        "Apakah anda sering merasa sakit pinggang?" to 0.55,
        "Apakah anda sering terlalu peka?" to 0.70,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        ANSWERS = FloatArray(PERTANYAAAN.size).apply {
            fill(-1f, 0)
        }

        val username = intent.getStringExtra("username")
        val programstudi = intent.getStringExtra("programstudi")
        SESSIONID = intent.getStringExtra("sessionID") ?: ""

        val questionHeaderText = findViewById<TextView>(R.id.questionHeaderText)
        questionHeaderText.text =
            "Nama : ${username}\nProgram Studi : ${programstudi}\nBPM:-\nHumidity:-\nTemperature:-\nMic:-"


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bpm = snapshot.child("bpm").getValue<Int>()
                val humidity = snapshot.child("humidity").getValue<Int>()
                val temperature = snapshot.child("temperature").getValue<Int>()
                val mic = snapshot.child("microphone").getValue<Boolean>()
                questionHeaderText.text =
                    "Nama : ${username}\nProgram Studi : ${programstudi}\nBPM:${bpm}\nHumidity:${humidity}\nTemperature:${temperature}\nMic:${mic}"

                questionViewModel.insertData(SESSIONID, "bpm", bpm ?: 0)
                questionViewModel.insertData(SESSIONID, "humidity", humidity ?: 0)
                questionViewModel.insertData(SESSIONID, "temperature", temperature ?: 0)
                questionViewModel.insertData(SESSIONID, "microphone", if (mic!!) 1 else 0)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("LOG", error.message)
            }

        })

        for (i in 0..PERTANYAAAN.size - 1) {
            val z = createItemPertanyaan(PERTANYAAAN.keys.elementAt(i), i)
            val insertPoint = findViewById<View>(R.id.containerPertanyaan) as ViewGroup
            insertPoint.addView(
                z,
                i
            )
        }

        findViewById<Button>(R.id.submitAnswer).setOnClickListener {

            var score = 0.0;

            for (i in 0..<PERTANYAAAN.size) {
                if (ANSWERS[i] == -1f) {
                    Toast.makeText(
                        baseContext,
                        "Anda belum menjawab pertanyaan ${PERTANYAAAN.keys.elementAt(i)}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
            }

            val cf1 = PERTANYAAAN.values.elementAt(0);
            val ans1 = ANSWERS[0];
            val r1 = cf1 * ans1;

            val cf2 = PERTANYAAAN.values.elementAt(1);
            val ans2 = ANSWERS[1];
            val r2 = cf2 * ans2;


            var oldcf = r1 + r2 * (1 - r1)


            for (i in 2..<PERTANYAAAN.size) {
                val cf = PERTANYAAAN.values.elementAt(i);
                val ans = ANSWERS[i];

//                if(ans == -1f){
//                    continue;
//                }

                val r = cf * ans;

                oldcf += r * (1 - oldcf)
            }

            startActivity(Intent(this, ResultActivity::class.java).apply {
                this.putExtra("answers", ANSWERS)
                this.putExtra("username", username)
                this.putExtra("programstudi", programstudi)
                this.putExtra("score", oldcf)
                this.putExtra("sessionID", SESSIONID)
            })
        }


    }

    fun createItemPertanyaan(pertanyaan: String, idx: Int): View {
        val inflater = this.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.item_pertanyaan, null)

        //Ganti Pertanyaan
        v.findViewById<TextView>(R.id.txtPertanyaan).text = pertanyaan
        v.findViewById<ChipGroup>(R.id.ansPertanyaan)
            .setOnCheckedStateChangeListener { chipGroup: ChipGroup, ints: MutableList<Int> ->

//                Log.d("LOG", "ANSWER ${ANSWERS.contentToString()}")
//                    v.findViewById<TextView>(R.id.txtScr).text = "TIDAK"

                val chipid = chipGroup.checkedChipId
                if (chipid == View.NO_ID) {
                    ANSWERS[idx] = -1f
                    return@setOnCheckedStateChangeListener
                }
                val chip: Chip = chipGroup.findViewById(chipid)
                if (chip.text.toString().contentEquals("Tidak Pernah")) {
                    ANSWERS[idx] = 0f
                    return@setOnCheckedStateChangeListener
                } else if (chip.text.toString().contentEquals("Jarang")) {
                    ANSWERS[idx] = 0.25f
                    return@setOnCheckedStateChangeListener
                } else if (chip.text.toString().contentEquals("Kadang-Kadang")) {
                    ANSWERS[idx] = 0.5f
                    return@setOnCheckedStateChangeListener
                } else if (chip.text.toString().contentEquals("Sering")) {
                    ANSWERS[idx] = 0.75f
                    return@setOnCheckedStateChangeListener
                } else if (chip.text.toString().contentEquals("Selalu")) {
                    ANSWERS[idx] = 1f
                    return@setOnCheckedStateChangeListener
                }
                return@setOnCheckedStateChangeListener
            }

        return v
    }


}