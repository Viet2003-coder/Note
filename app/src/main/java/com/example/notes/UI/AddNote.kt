package com.example.notes.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.Model.Note
import com.example.notes.databinding.ActivityAddNoteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNote : AppCompatActivity() {
    private var username1: String = ""
    private lateinit var binding : ActivityAddNoteBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSave.setOnClickListener {
            SaveNote()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun SaveNote() {
        dbRef= FirebaseDatabase.getInstance().getReference("Notes")
        username1=intent.getStringExtra("usernameadd").toString()
        var title=binding.edtTitle.text.toString()
        var content = binding.edtContent.text.toString()
        var noteId=dbRef.push().key!!
        val note= Note(noteId, username1, title, content)
        dbRef.child(noteId).setValue(note).addOnCompleteListener {
            Toast.makeText(this@AddNote,"Đã thêm thành công", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { err->
            Toast.makeText(this@AddNote,"Lỗi ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }
}