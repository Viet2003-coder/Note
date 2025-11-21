package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    private lateinit var dbr: DatabaseReference
    // TODO: Rename and change types of parameters
    private var username: String?=null
    private lateinit var adapter : NoteAdapter
    private var list = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           username= it.getString("usernamelogin")
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        var btnAdd= view.findViewById<FloatingActionButton>(R.id.btnAdd)
        var rcv=view.findViewById<RecyclerView>(R.id.rvNotes)
        adapter= NoteAdapter(list,object : NoteAdapter.OnItemclickistener{
            override fun onItemClick(note: Note) {
                val intent = Intent(requireContext(), Detail::class.java)
                intent.putExtra("noteid",note.id)
                startActivity(intent)
            }

        })
        val spanCount = if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) 3 else 2
        rcv.layoutManager = GridLayoutManager(requireContext(), spanCount)
        rcv.adapter=adapter
        dbr = FirebaseDatabase.getInstance().getReference("Notes")
        val edtSearch=view.findViewById<EditText>(R.id.etSearch)
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
        loadNotes()
        btnAdd.setOnClickListener {
            val intent = Intent(requireActivity(), AddNote::class.java)
            intent.putExtra("usernameadd",username)
            startActivity(intent)
        }
        return view
    }

    private fun loadNotes() {
        dbr.orderByChild("username").equalTo(username).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (data in snapshot.children) {
                    val note = data.getValue(Note::class.java)
                    note?.id =data.key
                    note?.let { list.add(it) }
                }
               adapter.updateData(list)
//                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Có thể thêm log để debug
                error.toException().printStackTrace()
            }
        })

    }
}
