package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityCreateNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNotes : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNotesBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.save.setOnClickListener {
            val title = binding.title.text.toString().trim()
            val description = binding.description.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Fill Both Blanks", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    // Generate a unique key for the note
                    val noteKey = databaseReference.child("users").child(user.uid).child("notes").push().key

                    val noteItem = CreateNote(title, description,noteKey?:"")
                    if (noteKey != null) {
                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey)
                            .setValue(noteItem)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()

                                    // Move to DashboardActivity
                                    val intent = Intent(this,Dashboard::class.java)
                                    intent.putExtra("note_saved", true) // Optional: Send status
                                    startActivity(intent)
                                    finish() // Close CreateNotes activity
                                } else {
                                    Toast.makeText(this, "Failed to save note!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }
}
