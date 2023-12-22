package com.example.movienow

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class DetailActivity : AppCompatActivity() {
    private var detailDesc: TextView? = null
    private var detailTitle: TextView? = null
    private var detailLang: TextView? = null
    private var detailImage: ImageView? = null
    private var deleteButton: FloatingActionButton? = null
    private var editButton: FloatingActionButton? = null
    private var key: String? = ""
    private var imageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailDesc = findViewById(R.id.detailDesc)
        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)
        deleteButton = findViewById(R.id.deleteButton)
        editButton = findViewById(R.id.editButton)


        val bundle = intent.extras
        if (bundle != null) {
            detailDesc?.text = bundle.getString("Description")
            detailTitle?.text = bundle.getString("Title")
            key = bundle.getString("Key")
            imageUrl = bundle.getString("Image")
            Glide.with(this).load(bundle.getString("Image")).into(detailImage!!)
        }

        deleteButton?.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("Android Tutorials")
            val storage = FirebaseStorage.getInstance()
            val storageReference = storage.getReferenceFromUrl(imageUrl!!)

            storageReference.delete().addOnSuccessListener {
                reference.child(key!!).removeValue()
                Toast.makeText(this@DetailActivity, "Deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, AdminActivity::class.java))
                finish()
            }
        }

        editButton?.setOnClickListener {
            val intent = Intent(this@DetailActivity, UpdateActivity::class.java)
                .putExtra("Title", detailTitle?.text.toString())
                .putExtra("Description", detailDesc?.text.toString())
                .putExtra("Image", imageUrl)
                .putExtra("Key", key)
            startActivity(intent)
        }

        val backClick = findViewById<ImageView>(R.id.backButton)
        backClick.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

    }
}
