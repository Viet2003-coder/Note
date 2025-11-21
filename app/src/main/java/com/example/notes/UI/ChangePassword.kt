package com.example.notes.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.ActivityChangePasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePassword : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnChangePassword.setOnClickListener {
            XuLyDoiMatKhau()
        }
    }

    private fun XuLyDoiMatKhau() {
        val oldPass = binding.edtOldPassword.text.toString().trim()
        val newPass = binding.edtNewPassword.text.toString().trim()
        val confirm = binding.edtConfirmPassword.text.toString().trim()

        when {
            oldPass.isEmpty() -> binding.edtOldPassword.error = "Nhập mật khẩu hiện tại"
            newPass.isEmpty() -> binding.edtNewPassword.error = "Nhập mật khẩu mới"
            confirm.isEmpty() -> binding.edtConfirmPassword.error = "Xác nhận lại mật khẩu"
            newPass != confirm -> binding.edtConfirmPassword.error = "Mật khẩu không khớp"
            else -> {
                val username=intent.getStringExtra("usernameinfo").toString()
                dbRef= FirebaseDatabase.getInstance().getReference("Users")
                dbRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val userSnapshot=snapshot.children.first()
                            val passcurrent=userSnapshot.child("password").value.toString()
                            val userKey=userSnapshot.key
                            if (passcurrent==oldPass){
                                dbRef.child(userKey!!).child("password").setValue(newPass).addOnSuccessListener {
                                    Toast.makeText(this@ChangePassword,"Cập nhật mật khẩu thành công",
                                        Toast.LENGTH_SHORT).show()
                                    binding.edtOldPassword.setText("")
                                    binding.edtNewPassword.setText("")
                                    binding.edtConfirmPassword.setText("")
                                }
                            } else{

                                Toast.makeText(this@ChangePassword,"Mật khẩu cũ bạn nhập không chính xác",
                                    Toast.LENGTH_SHORT).show()
                                binding.edtOldPassword.requestFocus()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChangePassword,"Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }
}