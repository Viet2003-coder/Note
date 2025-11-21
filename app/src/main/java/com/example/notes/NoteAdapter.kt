package com.example.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(var list : ArrayList<Note>,val listlener : OnItemclickistener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var fullList = ArrayList<Note>()
    init {
        fullList.addAll(list)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList : ArrayList<Note>){
        list=newList
        fullList.clear()
        fullList.addAll(newList)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun fillter(text: String){
        val filteredList= ArrayList<Note>()
        if (text.isEmpty()){
            filteredList.addAll(fullList)
        } else{
            for (note in fullList){
                if (note.title.orEmpty().lowercase().contains(text.lowercase())|| note.content.orEmpty().lowercase().contains(text.lowercase())){
                    filteredList.add(note)
                }
            }
        }
        list=filteredList
        notifyDataSetChanged()
    }
    interface OnItemclickistener{
        fun onItemClick(note: Note)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.noteitem,parent,false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.title.text=list[position].title
        holder.content.text=list[position].content
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        val formattedTime = sdf.format(Date(list[position].timestamp))
        holder.time.text=formattedTime
    }

    override fun getItemCount(): Int {
       return list.size
    }

   inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title=itemView.findViewById<TextView>(R.id.tvTitle)
        var content=itemView.findViewById<TextView>(R.id.tvContent)
        var time=itemView.findViewById<TextView>(R.id.tvTimestamp)
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listlener.onItemClick(list[position])
                }
            }
        }
    }
}