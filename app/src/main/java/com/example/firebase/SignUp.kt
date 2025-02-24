package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firebase.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        binding.register.setOnClickListener {
            val name=binding.etName.text.toString()
            val email=binding.etEmail.text.toString()
            val password=binding.etPassword.text.toString()
            val confirmpassword=binding.etConfirmPassword.text.toString()


            if(name.isEmpty()||email.isEmpty() ||password.isEmpty()||confirmpassword.isEmpty())
            {
                Toast.makeText(this, "Please Fill The blanks", Toast.LENGTH_LONG).show()
            }else if(password!=confirmpassword)
            {
                Toast.makeText(this, "Confirm password Must be Same", Toast.LENGTH_LONG).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task ->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(this, "Registration is Successfull", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "Registratin Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}