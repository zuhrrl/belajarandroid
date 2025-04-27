package com.sobodigital.zulbelajarandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    private lateinit var rvDestinations: RecyclerView
    private var destinations: MutableList<Destination> = mutableListOf()

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
        destinations = mutableListOf(Destination(name = "Labuan Bajo", R.drawable.image1, "Labuan Bajo adalah sebuah kota nelayan dan ibu kota Kabupaten Manggarai Barat di Nusa Tenggara Timur, Indonesia. Kota ini dikenal sebagai gerbang utama menuju Taman Nasional Komodo, tempat habitat alami satu-satunya bagi hewan purba Komodo. Labuan Bajo juga merupakan destinasi wisata populer dengan keindahan alam bawah laut yang luar biasa, menjadikannya tempat yang sempurna untuk snorkeling dan diving. \n" +
                "Lebih Detail:\n" +
                "\n" +
                "    Lokasi:\n" +
                "    Labuan Bajo terletak di ujung barat pulau Flores, tepatnya di Kecamatan Komodo, Kabupaten Manggarai Barat, Provinsi NTT. \n" +
                "\n" +
                "Taman Nasional Komodo:\n" +
                "Labuan Bajo merupakan pintu gerbang menuju Taman Nasional Komodo, yang terkenal dengan Komodo, naga purba terbesar di dunia. \n" +
                "Keindahan Bawah Laut:\n" +
                "Selain Komodo, Labuan Bajo juga memiliki keindahan bawah laut yang memukau, dengan terumbu karang yang kaya dan beragam jenis biota laut. \n" +
                "Wisata Populer:\n" +
                "Labuan Bajo telah menjadi destinasi wisata superprioritas di Indonesia, menarik banyak wisatawan domestik dan internasional. \n" +
                "Aktivitas Wisata:\n" +
                "Wisatawan dapat menikmati berbagai aktivitas di Labuan Bajo seperti:\n" +
                "\n" +
                "    Snorkeling dan diving di perairan Taman Nasional Komodo. \n" +
                "\n" +
                "Menjelajahi Pulau Komodo dan melihat Komodo secara langsung. \n" +
                "Berwisata ke Pantai Pink (Pantai Merah) dengan pasir merah muda yang unik. \n" +
                "Melihat sunset di Bukit Cinta atau Pulau Padar. \n" +
                "Menjelajahi desa wisata Loha. \n" +
                "\n" +
                "Ciri Khas:\n" +
                "Labuan Bajo juga dikenal sebagai \"Kota Seribu Sunset\" dan \"Sepotong Surga yang Jatuh ke Bumi\" karena keindahan alamnya yang memukau. \n" +
                "Pengalaman Unik:\n" +
                "Labuan Bajo menawarkan pengalaman wisata yang unik dan tak terlupakan, mulai dari melihat Komodo hingga menikmati keindahan bawah laut. \n" +
                "\n" +
                "    Deskripsi Labuan Bajo - Kompasiana.com\n" +
                "    16 Agu 2023 — Labuan Bajo adalah sebuah kota kecil yang terletak di barat Pulau Flores, Indonesia. ... Labuan Bajo adalah tujuan popu...\n" +
                "    Kompasiana.com\n" +
                "\n" +
                "Labuan Bajo - Wikipedia\n" +
                "Terjemahan — Labuan Bajo adalah kota nelayan yang terletak di ujung barat pulau besar Flores di provinsi Nusa Tenggara Timur, Indones...\n" +
                "en.wikipedia.org\n" +
                "Labuan Bajo, Komodo, Manggarai Barat - Wikipedia\n" +
                "Labuan Bajo merupakan salah satu kelurahan yang berada di Kecamatan Komodo, Kabupaten Manggarai Barat, Provinsi Nusa Tenggara Timu...\n" +
                "Wikipedia\n" +
                "\n" +
                "    Tampilkan semua\n" +
                "\n"))

        showRecyclerList(destinations)
    }
}