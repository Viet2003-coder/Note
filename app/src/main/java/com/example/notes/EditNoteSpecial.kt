package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.JavaHeapDumpRequestBuilder
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.strictmode.FragmentStrictMode
import com.example.notes.databinding.ActivityEditNoteSpecialBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditNoteSpecial : AppCompatActivity() {
    private lateinit var dbf: DatabaseReference
    private var note_id : String =""
    private lateinit var binding: ActivityEditNoteSpecialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteSpecialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        note_id=intent.getStringExtra("note_id_detail").toString()
        dbf= FirebaseDatabase.getInstance().getReference("SpecialNotes").child(note_id)
        binding.btnUpdateSpecial.setOnClickListener {
            ConfirmEvent()
        }
        binding.btnBackSpecial.setOnClickListener {
            finish()
        }
        LoadData()
    }

    private fun ConfirmEvent() {
        var title=binding.edtTitleUpdateSpecial.text.toString().trim()
        var content=binding.edtContentUpdateSpecial.text.toString().trim()
        if (title.isEmpty()){
            binding.edtTitleUpdateSpecial.error="Không để trống"
            return
        }
        if (content.isEmpty()){
            binding.edtContentUpdateSpecial.error="Không để trống"
            return
        }
        AlertDialog.Builder(this)
            .setTitle("Bạn có muốn lưu thay đổi")
            .setMessage("Xác nhận")
            .setPositiveButton("Có"){dialog, _ ->
                var updateMap=mapOf(
                   "title" to title,
                   "content" to content
               )
                dbf.updateChildren(updateMap).addOnSuccessListener {
                    Toast.makeText(this@EditNoteSpecial,"Đã thay đổi", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { err->
                    Toast.makeText(this@EditNoteSpecial,"Lỗi thay đổi ${err.message}", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Không"){dialog, _ -> dialog.dismiss()}.show()
    }

    private fun LoadData() {
        dbf.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var note=snapshot.getValue(SpecialNote::class.java)
                    binding.edtContentUpdateSpecial.setText(note?.content)
                    binding.edtTitleUpdateSpecial.setText(note?.title)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditNoteSpecial,"Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}