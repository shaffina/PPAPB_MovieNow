package com.example.movienow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateActivity : AppCompatActivity() {

    private lateinit var updateImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var updateDesc: EditText
    private lateinit var updateTitle: EditText

    private var uri: Uri? = null
    private var imageUrl: String? = null
    private var key: String? = null
    private var oldImageURL: String? = null
    private var databaseReference: DatabaseReference? = null
    private var storageReference: StorageReference? = null
    private var currentImageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        updateButton = findViewById(R.id.updateButton)
        updateDesc = findViewById(R.id.updateDesc)
        updateImage = findViewById(R.id.updateImage)
        updateTitle = findViewById(R.id.updateTitle)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    uri = data?.data
                    updateImage.setImageURI(uri)
                } else {
                    Toast.makeText(this@UpdateActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            }

        val bundle = intent.extras
        if (bundle != null) {
            currentImageUrl = bundle.getString("Image")
            Glide.with(this@UpdateActivity).load(currentImageUrl).into(updateImage)
            updateTitle.setText(bundle.getString("Title"))
            updateDesc.setText(bundle.getString("Description"))
            key = bundle.getString("Key")
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key!!)

        updateImage.setOnClickListener(View.OnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        })

        updateButton.setOnClickListener(View.OnClickListener {
            saveData()
            val intent = Intent(this@UpdateActivity, AdminActivity::class.java)
            startActivity(intent)
        })
    }

    private fun saveData() {
        if (uri != null) {
            // Lanjutkan dengan pemrosesan gambar
            storageReference = FirebaseStorage.getInstance().reference.child("Android Images").child(uri!!.lastPathSegment ?: "")
            val builder = AlertDialog.Builder(this@UpdateActivity)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            dialog.show()

            storageReference!!.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isComplete);
                val urlImage = uriTask.result
                imageUrl = urlImage.toString()
                updateData()
                dialog.dismiss()
            }.addOnFailureListener { e ->
                dialog.dismiss()
                Toast.makeText(this@UpdateActivity, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Kasus ketika uri adalah null
            // Handle logika pembaruan tanpa pemilihan gambar baru
            updateDataWithoutImage()
        }
    }

    private fun updateDataWithoutImage() {
        val title = updateTitle.text.toString().trim()
        val desc = updateDesc.text.toString().trim()

        val dataClass = DataClass(title, desc, currentImageUrl)

        databaseReference!!.setValue(dataClass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this@UpdateActivity, "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this@UpdateActivity, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateData() {
        val title = updateTitle.text.toString().trim()
        val desc = updateDesc.text.toString().trim()

        val dataClass = DataClass(title, desc, imageUrl)

        databaseReference!!.setValue(dataClass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL!!)
                reference.delete()
                Toast.makeText(this@UpdateActivity, "Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this@UpdateActivity, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
