package com.example.notes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notes.databinding.ActivityDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var username : String ?= null
    var note_id : String =""
    private lateinit var dbr: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        note_id= intent.getStringExtra("noteid").toString()
        dbr= FirebaseDatabase.getInstance().getReference("Notes").child(note_id)
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Xác nhận")
            builder.setMessage("Bạn có muốn xóa ghi chú này ?")
            builder.setPositiveButton("Có"){dialog, which ->
                dbr.removeValue().addOnSuccessListener {
                    Toast.makeText(this,"Đã xóa thành công", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    finish()
                }
            }
            builder.setNegativeButton("Không"){ dialog, which -> dialog.dismiss() }
            var dialog=builder.create()
            dialog.show()
        }
        binding.btnEdit.setOnClickListener {
            var intent = Intent(this, UpdateNote::class.java)
            intent.putExtra("idnote",note_id)
            startActivity(intent)
        }
        loadData()
        binding.btnspecial.setOnClickListener {
            EventAddSpecialNote()
        }
    }

    private fun EventAddSpecialNote() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận")
            .setMessage("Bạn có muốn thêm vào ghi chú đặc biệt?")
            .setPositiveButton("Có") { dialog,_ ->
                var titleSpecial=binding.tvDetailTitle.text.toString()
                var contentSpecial=binding.tvDetailContent.text.toString()
                var timeSpecial=binding.tvTime.text.toString()
                var specialId=dbr.push().key!!
                var specialNote = SpecialNote(specialId, note_id,username,titleSpecial,contentSpecial,timeSpecial)
                dbr= FirebaseDatabase.getInstance().getReference("SpecialNotes")
                dbr.orderByChild("id_note").equalTo(note_id).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            Toast.makeText(this@Detail,"Đã có ghi chú đặc biệt này", Toast.LENGTH_SHORT).show()
                            return
                        } else{
                            dbr.child(specialId).setValue(specialNote).addOnSuccessListener {
                                Toast.makeText(this@Detail,"Thêm thành công ghi chú đặc biệt", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }.addOnFailureListener {
                                    err->
                                Toast.makeText(this@Detail,"Lỗi ${err.message}", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Detail,"Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Không") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun loadData() {
        dbr.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val note=snapshot.getValue(Note::class.java)
                    username=note ?.username
                    binding.tvDetailTitle.setText(note?.title.toString())
                    binding.tvDetailContent.text=note?.content
                    val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
                    val formattedTime = sdf.format(note?.timestamp)
                    binding.tvTime.text=formattedTime.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Detail,"Lỗi${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}