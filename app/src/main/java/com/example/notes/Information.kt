package com.example.notes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Information : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private var username : String?=null
    private var list = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username= it.getString("usernamelogin")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_information, container, false)
        // Bước 2: Ánh xạ view (findViewById từ view đã inflate)
        val txtUsername = view.findViewById<TextView>(R.id.txtUsername)
        val txtTotalNotes = view.findViewById<TextView>(R.id.txtTotalNotes)
        val layoutChangePassword = view.findViewById<LinearLayout>(R.id.layoutChangePassword)
        val layoutLogout = view.findViewById<LinearLayout>(R.id.layoutLogout)
        dbRef= FirebaseDatabase.getInstance().getReference("Notes")
        dbRef.orderByChild("username").equalTo(username).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    txtTotalNotes.text = snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
        // Bước 3: Gán dữ liệu giả (ví dụ từ Firebase hoặc SharedPreferences)
        txtUsername.text = username
        // Bước 4: Xử lý sự kiện
        layoutChangePassword.setOnClickListener {
            var intent= Intent(requireActivity(), ChangePassword::class.java)
            intent.putExtra("usernameinfo",username)
            startActivity(intent)
        }

        layoutLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
        // Bước 5: Trả về view cho fragment
        return view
    }

}