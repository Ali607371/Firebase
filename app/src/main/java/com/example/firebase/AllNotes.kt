package com.example.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.emptyLongSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityAllNotesBinding
import com.example.firebase.databinding.DialogeUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllNotes : AppCompatActivity(),NoteAdapter.OnItemClickListener {
    private lateinit var binding: ActivityAllNotesBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private val noteList = mutableListOf<CreateNote>()  // ✅ Global mutable list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter
        adapter = NoteAdapter(noteList, this@AllNotes)
        recyclerView.adapter = adapter

        // Initialize Firebase Database & Authentication
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noteList.clear()  // ✅ Clear list before adding new data
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(CreateNote::class.java)
                        note?.let { noteList.add(it) }
                    }
                    noteList.reverse()  // ✅ Show the latest note first
                    adapter.notifyDataSetChanged()  // ✅ Notify adapter after data update
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    override fun onDeleteClick(noteid: String) {
        val currentuser = auth.currentUser
        currentuser?.let { user ->
            val notereference = databaseReference.child("users").child(user.uid).child("notes")
            notereference.child(noteid)
                .removeValue()

        }
    }

    override fun onUpdateClick(noteid: String, curruenttitle: String, currunetdescription: String) {
        val dialogbinding=DialogeUpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog=AlertDialog.Builder(this).setView(dialogbinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){ dialog,_->
                val newtitle=dialogbinding.updateTitle.text.toString()
                val newdescription=dialogbinding.updateDescription.text.toString()
                updateNoteDatabase(noteid,newtitle,newdescription)
               dialog.dismiss()
            }
            .setNegativeButton("Cancel"){  dialoge,_ ->
                dialoge.dismiss()
            }
            .create()
        dialogbinding.updateTitle.setText(curruenttitle)
        dialogbinding.updateDescription.setText("currunetdescription")
        dialog.show()
    }

    private fun updateNoteDatabase(noteid: String, newtitle: String, newdescription: String) {
        val currentuser = auth.currentUser
        currentuser?.let { user ->
            val notereference = databaseReference.child("users").child(user.uid).child("notes")
            val updatenote = CreateNote(newtitle, newdescription, noteid)
            notereference.child(noteid).setValue(updatenote).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Note Update Successfuly", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed Update Note  Failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}