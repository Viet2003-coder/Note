package com.example.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.NoteAdapter.OnItemclickistener

class SpecialNoteAdapter(var list: ArrayList<SpecialNote>,val listlener : OnItemclickistener) : RecyclerView.Adapter<SpecialNoteAdapter.NoteSpecialViewHoelder>() {
    private var fullList = ArrayList< SpecialNote>()
    init {
        fullList.addAll(list)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList : ArrayList<SpecialNote>){
        list=newList
        fullList.clear()
        fullList.addAll(newList)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun fillter(text: String){
        val filteredList= ArrayList<SpecialNote>()
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
        fun onItemClick(note: SpecialNote)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteSpecialViewHoelder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.special_item,parent,false)
        return NoteSpecialViewHoelder(view)
    }

    override fun onBindViewHolder(
        holder: NoteSpecialViewHoelder,
        position: Int
    ) {
        holder.titleSpecial.text=list[position].title
        holder.content.text=list[position].content
        holder.time.text=list[position].time
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NoteSpecialViewHoelder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titleSpecial=itemView.findViewById<TextView>(R.id.tvTitleSpecial)
        var content=itemView.findViewById<TextView>(R.id.tvContentSpecial)
        var time=itemView.findViewById<TextView>(R.id.tvTimeSpecial)
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