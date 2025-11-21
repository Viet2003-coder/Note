package com.example.notes

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.note.User
import com.example.notes.databinding.ActivityRegister1Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Register1 : AppCompatActivity() {
    lateinit var binding: ActivityRegister1Binding
    private lateinit var ds: ArrayList<User>
    private lateinit var dbRef: DatabaseReference
    @SuppressLint("UseKtx")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegister1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef= FirebaseDatabase.getInstance().getReference("Users")
        binding.btnregiter.setOnClickListener {
            val inputUsename=binding.edtusernamergt.text.toString()
            val inputPassword=binding.edtpasswordrgt.text.toString()
            binding.edtusernamergt.addTextChangedListener {
                binding.edtusernamergt.error = null
                binding.edtusernamergt.setTextColor(Color.BLACK)
            }
            binding.edtpasswordrgt.addTextChangedListener {
                binding.edtpasswordrgt.error = null
                binding.edtpasswordrgt.setTextColor(Color.BLACK)
            }
            if (inputUsename.isEmpty()){
                Toast.makeText(this@Register1,"Không được để trống username", Toast.LENGTH_SHORT).show()
                binding.edtusernamergt.error = "Vui lòng điền lại"
                binding.edtusernamergt.requestFocus()
            }
             else if (inputPassword.isEmpty()){
                Toast.makeText(this@Register1,"Không được để trống password", Toast.LENGTH_SHORT).show()
                binding.edtpasswordrgt.error = "Vui lòng điền lại"
                binding.edtpasswordrgt.requestFocus()
            } else{
                dbRef.orderByChild("username").equalTo(inputUsename).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            //có ngời dùng trùng username
                            Toast.makeText(this@Register1,"Username đã tồn tại", Toast.LENGTH_SHORT).show()
                            binding.edtusernamergt.requestFocus()
                            binding.edtusernamergt.error = "Vui lòng điền lại"
                        } else{
                            XuLyRegister()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Register1,"Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
    }

    private fun XuLyRegister() {
        val usename=binding.edtusernamergt.text.toString()
        val password=binding.edtpasswordrgt.text.toString()
        //đẩy dữ liệu
        val userId=dbRef.push().key!!
        val user= User(userId,usename,password)
        dbRef.child(userId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this,"Đăng ký thành công", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {err->
                Log.e("FIREBASE", "Lỗi đăng nhập: ${err.message}")
                Toast.makeText(this,"Lỗi đăng nhập ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }
}