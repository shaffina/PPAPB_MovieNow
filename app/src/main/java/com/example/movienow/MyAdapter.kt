package com.example.movienow

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyAdapter(private val context: Context, private var dataList: MutableList<DataClass?>?) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataItem = dataList?.get(position)
        Glide.with(context).load(dataItem?.dataImage).into(holder.recImage)
        holder.recTitle.text = dataItem?.dataTitle
        holder.recDesc.text = dataItem?.dataDesc
        holder.recCard.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Image", dataItem?.dataImage)
            intent.putExtra("Description", dataItem?.dataDesc)
            intent.putExtra("Title", dataItem?.dataTitle)
            intent.putExtra("Key", dataItem?.key)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    fun searchDataList(searchList: ArrayList<DataClass?>) {
        dataList = searchList.toMutableList()
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recImage: ImageView = itemView.findViewById(R.id.recImage)
    var recTitle: TextView = itemView.findViewById(R.id.recTitle)
    var recDesc: TextView = itemView.findViewById(R.id.recDesc)
    var recCard: CardView = itemView.findViewById(R.id.recCard)
}
