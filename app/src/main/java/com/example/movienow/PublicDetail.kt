package com.example.movienow

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class PublicDetail : AppCompatActivity() {
    private var detailDesc: TextView? = null
    private var detailTitle: TextView? = null
    private var detailImage: ImageView? = null
    private var key: String? = ""
    private var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_detail)

        detailDesc = findViewById(R.id.detailDesc)
        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)

        val bundle = intent.extras
        if (bundle != null) {
            detailDesc?.text = bundle.getString("Description")
            detailTitle?.text = bundle.getString("Title")
            key = bundle.getString("Key")
            imageUrl = bundle.getString("Image")
            Glide.with(this).load(bundle.getString("Image")).into(detailImage!!)
        }
        val backClick = findViewById<ImageView>(R.id.backButton)
        backClick.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
