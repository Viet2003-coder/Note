package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notes.databinding.ActivityDetailSpecialBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailSpecial : AppCompatActivity() {
    private lateinit var dbr : DatabaseReference
    private var note_id : String =""
    private lateinit var binding: ActivityDetailSpecialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailSpecialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        note_id=intent.getStringExtra("notespecial_id").toString()
        binding.btnBackSpecialDetail.setOnClickListener {
            finish()
        }
        binding.btnDeleteSpecial.setOnClickListener {
            AlertDialog.Builder(this@DetailSpecial)
                .setTitle("Bạn có muốn xóa?")
                .setMessage("Xác nhận")
                .setPositiveButton("Có"){dialog, _ ->
                    dbr= FirebaseDatabase.getInstance().getReference("SpecialNotes")
                    dbr.child(note_id).removeValue().addOnSuccessListener {
                        Toast.makeText(this@DetailSpecial,"Đã xóa thành công", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        finish()
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this@DetailSpecial,"Lỗi xóa ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("Không"){dialog, _-> dialog.dismiss() }.show()
        }
        binding.btnEditSpecial.setOnClickListener {
            var intent = Intent(this@DetailSpecial, EditNoteSpecial::class.java)
            intent.putExtra("note_id_detail",note_id)
            startActivity(intent)
        }
        LoadData()
    }

    private fun LoadData() {
        dbr= FirebaseDatabase.getInstance().getReference("SpecialNotes")
        dbr.child(note_id).addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var noteSpecial = snapshot.getValue(SpecialNote::class.java)
                    binding.tvTimeSpecial.text=noteSpecial?.time
                    binding.tvDetailTitleSpecial.text=noteSpecial?.title
                    binding.tvDetailContentSpecial.text=noteSpecial?.content
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailSpecial,"${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        LoadData()
    }
}