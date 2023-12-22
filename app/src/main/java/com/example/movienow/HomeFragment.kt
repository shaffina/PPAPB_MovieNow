package com.example.movienow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var dataList: MutableList<DataClass?>? = null
    private var adapter: adapter? = null
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(activity, 1)
        recyclerView?.layoutManager = gridLayoutManager

        dataList = ArrayList()
        adapter = adapter(requireContext(), dataList)
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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hapus listener saat fragment dihancurkan untuk mencegah memory leak
        eventListener?.let {
            databaseReference?.removeEventListener(it)
        }
    }
}

