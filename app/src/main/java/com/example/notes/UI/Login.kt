package com.example.notes.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.btnlogin.setOnClickListener {
            val username = binding.edtusername.text.toString().trim()
            val password = binding.edtpassword.text.toString().trim()

            // ✅ Kiểm tra trống
            if (username.isEmpty()) {
                binding.edtusername.error = "Nhập tên đăng nhập"
                binding.edtusername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtpassword.error = "Nhập mật khẩu"
                binding.edtpassword.requestFocus()
                return@setOnClickListener
            }

            // ✅ Kiểm tra đăng nhập Firebase
            XuLyLogin(username, password)
        }
    }

    private fun XuLyLogin(usename: String,password: String) {
        dbRef.orderByChild("username").equalTo(usename).addListenerForSingleValueEvent(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var loginSucess=false
                    for (user in snapshot.children){
                        val dbPassword= user.child("password").getValue(String::class.java)
                        if (dbPassword==password){
                            loginSucess=true
                            break
                        }
                    }
                    if (loginSucess) {
                        Toast.makeText(this@Login, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        var intent = Intent(this@Login, MainActivity::class.java)
                        intent.putExtra("usernamelogin",usename)
                         startActivity(intent)
                    } else {
                        Toast.makeText(this@Login, "Sai mật khẩu!", Toast.LENGTH_SHORT).show()
                        binding.edtpassword.error = "Sai mật khẩu"
                        binding.edtpassword.requestFocus()
                    }
                } else {
                    Toast.makeText(this@Login, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show()
                    binding.edtusername.error = "Không tồn tại"
                    binding.edtusername.requestFocus()
                }
                }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Login,"Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}