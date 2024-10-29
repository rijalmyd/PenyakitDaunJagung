package com.app.penyakitdaunjagung.data.model

import com.app.penyakitdaunjagung.R

val diseaseList = listOf(
    Disease(
        classId = "sehat",
        name = "Sehat",
        nameLatin = "",
        imageResourceId = R.drawable.sehat,
        indication = "Daun jagung sehat berwarna hijau tua yang pekat dan segar; " +
                "tumbuh tegak, lurus, dan tidak melengkung; " +
                "permukaan daun halus tanpa bercak, kerutan, atau tanda penyakit; " +
                "ujung daun tidak mengering, tetap lembab dan elastis; " +
                "daun bersifat elastis dan lentur, tidak rapuh atau mudah patah.",
        treatment = emptyList()
    ),
    Disease(
        classId = "hawar-daun",
        name = "Hawar Daun",
        nameLatin = "Sethosphaeria turcica",
        imageResourceId = R.drawable.hawar_daun,
        indication = "Awal terinfeksinya hawar daun, menunjukkan gejala berupa bercak " +
                "kecil, berbentuk oval kemudian bercak semakin memanjang berbentuk " +
                "ellips dan berkembang menjadi nekrotik (disebut hawar), warnanya " +
                "hijau keabu-abuan atau coklat. Panjang hawar 2,5-15 cm, bercak " +
                "muncul dimulai dari daun terbawah kemudian berkembang menuju " +
                "daun atas. Infeksi berat akibat serangan hawar daun dapat " +
                "mengakibatkan tanaman jagung cepat mati atau mengering.",
        treatment = listOf(
            "Menanam verietas tahan hawar daun, seperti bisma, pioner-2, " +
                    "pioner-14, semar-2 dan semar-5.",
            "Pemusnahan seluruh bagian tanaman sampai ke " +
                    "akarnya(Eradikasi tanaman) pada tanaman terinfeksi bercak daun. ",
            "Penyemprotan fungsida menggunakan bahan aktif atau " +
                    "dithicarbamate."

        ),
    ),
    Disease(
        classId = "bercak-daun",
        name = "Bercak Daun",
        nameLatin = "Bipolaris maydis Syn.",
        imageResourceId = R.drawable.becak_daun,
        indication = "Penyakit bercak daun pada tanaman jagung dikenal dua tipe menurut " +
                "ras patogennya yaitu ras O dan T. Ras O bercak berwarna coklat " +
                "kemerahan berukuran 0,6 x (1,2-1,9) cm, sedangkan Ras T bercak " +
                "berukuran lebih besar yaitu (0,6-1,2)x(0,6-2,7) cm. Ras T berbentuk " +
                "kumparan, bercak berwarna hijau kuning atau klorotik kemudian " +
                "menjadi coklat kemerahan. Kedua ras ini, ras T lebih berbahaya " +
                "(virulen) dibanding ras O. Serangan pada bibit tanaman menyebabkan " +
                "tanaman menjadi layu atau mati dalam waktu 3-4 minggu setelah " +
                "tanam.",
        treatment = listOf(
            "Menanam varietas tahan serangan bercak daun, seperti Bima-1, " +
                    "Srikandi Kuning-1, Sukmaraga atau Palakka.",
            "Pemusnahan seluruh bagian tanaman sampai akarnya (Eradikasi " +
                    "tanaman) pada tanaman terinfeksi bercak daun.",
            "Penggunaan fungisida menggunakan bahan aktif mancozeb atau " +
                    "karbendazim."
        )
    ),
    Disease(
        classId = "karat-daun",
        name = "Karat Daun",
        nameLatin = "Puccinia Polysora",
        imageResourceId = R.drawable.karat_daun,
        indication = "Bercak-bercak kecil (uredinia) berbentuk bulat sampai oval terdapat di " +
                "permukaan daun jagung bagian atas maupun bawah, uredinia " +
                "menghasilkan uredospora berbentuk bulat atau oval serta berperan " +
                "penting sebagai sumber inokulum dalam menginfeksi Tanaman " +
                "jagung lainnya, sebarannya melalui angin. Penyakit karat dapat terjadi " +
                "di dataran rendah sampai tinggi, infeksinya berkembang baik pada " +
                "musim penghujan atau musim kemarau.",
        treatment = listOf(
            "Menanam varietas tahankarat daun, seperti Lamuru, Sukmaraga, " +
                    "Palakka, Bima-1 atau Semar-10.",
            "Pemusnahan seluruh bagian tanaman sampai ke akarnya " +
                    "(Eradikasi tanaman) pada tanaman terinfeksi karat daun maupun " +
                    "gulma.",
            "Penyemprotan fungisida menggunakan bahan aktif benomil. " +
                    "(Sudjono, 2018)"
        )
    )
)