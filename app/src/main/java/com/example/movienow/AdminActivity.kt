package com.example.movienow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminActivity : AppCompatActivity() {
    private var fab: FloatingActionButton? = null
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null
    private var recyclerView: RecyclerView? = null
    private var dataList: MutableList<DataClass?>? = null
    private var adapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView?.layoutManager = gridLayoutManager

        dataList = ArrayList()
        adapter = MyAdapter(this, dataList)
        recyclerView?.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials")

        eventListener = databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList?.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.key = itemSnapshot.key
                    dataList?.add(dataClass)
                }
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })

        fab?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        })
    }

}
