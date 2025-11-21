package com.example.notes.UI

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.Adapter.SpecialNoteAdapter
import com.example.notes.Model.SpecialNote
import com.example.notes.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpecialNotes.newInstance] factory method to
 * create an instance of this fragment.
 */
class SpecialNotes : Fragment() {
    private var list = ArrayList<SpecialNote>()
    private lateinit var adapter: SpecialNoteAdapter
    private var username : String ?= null
    private lateinit var dbr : DatabaseReference
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
        val view = inflater.inflate(R.layout.fragment_special_notes, container, false)

        val rcv = view.findViewById<RecyclerView>(R.id.rvNotesSpecial)
        adapter = SpecialNoteAdapter(list, object : SpecialNoteAdapter.OnItemclickistener {
            override fun onItemClick(note: SpecialNote) {
                val intent = Intent(requireContext(), DetailSpecial::class.java)
                intent.putExtra("notespecial_id", note.id)
                startActivity(intent)
            }
        })   // ✔️ KHỞI TẠO TRƯỚC
        val spanCount = if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) 3 else 2
        rcv.layoutManager = GridLayoutManager(requireContext(), spanCount)
        rcv.adapter = adapter
        val edtSearch=view.findViewById<EditText>(R.id.edtSearchSpecial)
        edtSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                adapter.fillter(s.toString())
            }

        })
        dbr = FirebaseDatabase.getInstance().getReference("SpecialNotes")

        loadSpecialNotes()  // ✔️ gọi function xử lý firebase sau
        return view
    }

    private fun loadSpecialNotes() {
        dbr.orderByChild("username").equalTo(username)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (data in snapshot.children) {
                        val special = data.getValue(SpecialNote::class.java)
                        special?.let { list.add(it) }
                    }
                    adapter.updateData(list)   // ✔️ giờ adapter chắc chắn đã khởi tạo
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}