package com.example.notes.UI

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.R
import com.example.notes.databinding.ActivityUpdateNoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateNote : AppCompatActivity() {
     var note_id: String =""
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var dbf : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        note_id = intent.getStringExtra("idnote").toString()
        loadNoteData()
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnUpdate.setOnClickListener {
            EditNote()
        }
    }
    private fun loadNoteData() {
        val ref = FirebaseDatabase.getInstance().getReference("Notes")

        ref.child(note_id).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val title = snapshot.child("title").value.toString()
                val content = snapshot.child("content").value.toString()
                binding.edtTitleUpdate.setText(title)
                binding.edtContentUpdate.setText(content)
            }
        }
    }
    private fun EditNote() {
        dbf= FirebaseDatabase.getInstance().getReference("Notes").child(note_id)
        var edtTitle=findViewById<EditText>(R.id.edtTitleUpdate)
        var edtContent=findViewById<EditText>(R.id.edtContentUpdate)
        var title=edtTitle.text.toString().trim()
        var content=edtContent.text.toString().trim()
        if (title.isEmpty()){
            binding.edtTitleUpdate.error="Không để trống"
            return
        }
        if (content.isEmpty()){
            binding.edtContentUpdate.error="Không để trống"
            return
        }
        val updateMap=mapOf(
            "title" to title,
            "content" to content
        )
        dbf.updateChildren(updateMap).addOnSuccessListener {
            Toast.makeText(this@UpdateNote,"Cập nhật thành công", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { err->
            Toast.makeText(this@UpdateNote,"Cập nhật không thành công ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }
}