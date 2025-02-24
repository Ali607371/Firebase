 package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

 class MainActivity : AppCompatActivity() {
     private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
     override fun onStart() {
         super.onStart()
         val currentuser: FirebaseUser? =auth.currentUser
         if(currentuser!=null)
         {
             startActivity(Intent(this,Dashboard::class.java))
             finish()
         }
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()


        binding.login.setOnClickListener {
          val email=binding.email.text.toString()
            val password=binding.password.text.toString()

            if(email.isEmpty()||password.isEmpty())
            {
                Toast.makeText(this, "Fill All Detail", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task ->
                        if(task.isSuccessful)
                        {
                            Toast.makeText(this, "Signin Successfull", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,Dashboard::class.java))
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this, "Signin Failed! ${task?.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }



        binding.signup.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }

    }
}