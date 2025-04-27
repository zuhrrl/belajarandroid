package com.sobodigital.zulbelajarandroid

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var rvDestinations: RecyclerView
    private var destinations: MutableList<Destination> = mutableListOf()
    private lateinit var btnProfile: ImageView

    private fun showRecyclerList(list: MutableList<Destination>) {
        rvDestinations.layoutManager = LinearLayoutManager(this)
        val adapter = DestinationAdapter(list)
        rvDestinations.adapter = adapter

        adapter.setOnItemClickCallback(object: DestinationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Destination) {
                Log.d("debug", "Okee click")
                val intent = Intent(this@MainActivity, DestinationDetailActivity::class.java)
                intent.putExtra("data", data)
                startActivity(intent)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        rvDestinations = findViewById(R.id.rv_destination)
        btnProfile = findViewById(R.id.btn_profile_home)

        destinations = mutableListOf(Destination(name = "Labuan Bajo", R.drawable.image1, "Labuan Bajo adalah sebuah kota nelayan di ujung barat Pulau Flores, Nusa Tenggara Timur, Indonesia, yang merupakan pintu gerbang menuju Taman Nasional Komodo"),
            Destination("Malioboro", R.drawable.image4, "Malioboro adalah jalan utama dan kawasan wisata yang sangat terkenal di Yogyakarta, dikenal sebagai pusat perbelanjaan dan tempat wisata budaya. Jalan ini membentang dari Tugu Yogyakarta hingga Titik Nol Kilometer."),
            Destination("Pantai Karimun Jawa", R.drawable.image2,"Pantai Karimunjawa terkenal dengan keindahan alamnya yang memukau, khususnya keindahan bawah laut dan pasir putihnya yang halus. Karimunjawa adalah kepulauan yang terletak di Laut Jawa, termasuk dalam wilayah Kabupaten Jepara, Jawa Tengah"),
            Destination("Gunung Prau", R.drawable.image3,"Gunung Prau adalah gunung di Dataran Tinggi Dieng, Jawa Tengah, Indonesia, yang terkenal dengan pemandangan matahari terbitnya. Puncak Gunung Prau memiliki ketinggian 2.590 meter di atas permukaan laut (mdpl)."),
            Destination("Lantai 2 Jogja", R.drawable.image5,"Lantai 2 di Jogja City Light memiliki beberapa nama tergantung pada jenis bangunan atau fasilitasnya. Jika dimaksudkan Jogja Citylight Mall, lantai 2 umumnya digunakan untuk berbagai tenant ritel seperti toko pakaian, toko aksesoris, dan toko makanan."),
            Destination("Pura Bali", R.drawable.image8,"Pura di Bali adalah tempat ibadah umat Hindu Bali yang dibangun sesuai dengan aturan, gaya, dan ritual khas arsitektur Bali. Pura-pura ini memiliki desain yang rumit dan kompleks, sering kali dengan gerbang, alun-alun, pagar batu, tangga, dan menara kecil (meru)."),
            Destination("Pantai Bali", R.drawable.image9,"Bali terkenal dengan berbagai pantai indah yang menarik bagi wisatawan. Beberapa pantai populer di Bali antara lain adalah Pantai Kuta, Pantai Sanur, Pantai Nusa Dua, Pantai Jimbaran, dan Pantai Seminyak"),
            Destination("Telaga Warna Dieng", R.drawable.image7,"Telaga Warna adalah sebuah telaga di kawasan Dataran Tinggi Dieng, Wonosobo, Jawa Tengah, yang terkenal karena warna airnya yang sering berubah-ubah. Fenomena ini terjadi karena kandungan belerang tinggi di air telaga yang dipengaruhi oleh sinar matahari."),
            Destination("Dieng Wonosobo", R.drawable.image6,"Dieng, atau Dataran Tinggi Dieng, adalah sebuah dataran tinggi vulkanik yang terletak di Jawa Tengah, Indonesia, terkenal dengan keindahan alamnya dan situs bersejarahnya."),
            Destination("Pantai Jogja", R.drawable.image10,"Pantai-pantai di Yogyakarta menawarkan pemandangan alam yang menakjubkan, dari pasir putih, tebing karang hingga ombak besar yang menantang."),
            )

        Log.d("DEV", destinations.size.toString())

        showRecyclerList(destinations)

        btnProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}