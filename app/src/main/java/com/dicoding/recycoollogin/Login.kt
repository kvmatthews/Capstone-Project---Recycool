package com.dicoding.recycoollogin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    private lateinit var editTextUsernameOrEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLog: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsernameOrEmail = findViewById(R.id.emailorusername)
        editTextPassword = findViewById(R.id.password)
        buttonLog = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.registerNow)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        }

        buttonLog.setOnClickListener {
            val usernameOrEmail = editTextUsernameOrEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter both username/email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            if (usernameOrEmail.contains('@')) {
                signInWithEmail(usernameOrEmail, password)
            } else {
                signInWithUsername(usernameOrEmail, password)
            }
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                handleSignInResult(task)
            }
    }

    private fun signInWithUsername(username: String, password: String) {
        val usernamesRef = FirebaseDatabase.getInstance().getReference("usernames")
        val query = usernamesRef.orderByChild("username").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val email = userSnapshot.child("email").value as String
                        signInWithEmail(email, password)
                        return
                    }
                } else {
                    Log.d("TAG", "No matching data for username: $username")
                }
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@Login, "Invalid username", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", "Error checking username: ${databaseError.message}")
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@Login, "Error checking username", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleSignInResult(task: Task<AuthResult>) {
        progressBar.visibility = View.INVISIBLE

        if (task.isSuccessful) {
            Log.d("TAG", "signInWithEmail:success")
            val user = auth.currentUser

            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.w("TAG", "signInWithEmail:failure", task.exception)
            Toast.makeText(
                this@Login,
                "Authentication failed. Check your username/email and password.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
