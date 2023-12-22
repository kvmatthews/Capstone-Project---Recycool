package com.dicoding.recycoollogin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.auth.UserProfileChangeRequest

class Register : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var buttonReg: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextUsername = findViewById(R.id.username)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.loginNow)
        databaseReference = FirebaseDatabase.getInstance().reference.child("usernames")

        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val username = editTextUsername.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi keunikan username
            checkUsernameAvailability(username, object : UsernameAvailabilityCallback {
                override fun onUsernameAvailable() {
                    // Username tersedia, lanjutkan dengan pembuatan akun
                    progressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this@Register) { task ->
                            progressBar.visibility = View.INVISIBLE
                            if (task.isSuccessful) {
                                // Registrasi berhasil, tampilkan pesan ke pengguna
                                Toast.makeText(
                                    this@Register,
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Set username to Firebase user profile
                                val user = auth.currentUser
                                user?.let {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build()
                                    it.updateProfile(profileUpdates)
                                }

                                // Simpan informasi tambahan ke database (jika diperlukan)
                                saveUserInfoToDatabase(user?.uid, username, email)

                                // Pindah ke MainActivity
                                val mainIntent = Intent(this@Register, MainActivity::class.java)
                                startActivity(mainIntent)
                                finish()
                            } else {
                                // Registrasi gagal, tampilkan pesan ke pengguna
                                Toast.makeText(
                                    this@Register,
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

                override fun onUsernameUnavailable() {
                    // Username sudah digunakan, berikan pesan kesalahan
                    Toast.makeText(
                        this@Register,
                        "Username not available. Choose another one.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    // Fungsi untuk memeriksa keunikan username
    private fun checkUsernameAvailability(username: String, callback: UsernameAvailabilityCallback) {
        val query = databaseReference.orderByValue().equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username sudah digunakan
                    callback.onUsernameUnavailable()
                } else {
                    // Username tersedia
                    callback.onUsernameAvailable()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Tangani kesalahan
                callback.onUsernameAvailable()
            }
        })
    }

    // Fungsi untuk menyimpan informasi tambahan ke database (jika diperlukan)
    private fun saveUserInfoToDatabase(userId: String?, username: String, email: String) {
        userId?.let {
            val userRef = databaseReference.child(it)
            userRef.child("username").setValue(username)
            userRef.child("email").setValue(email)
            // Anda dapat menambahkan lebih banyak informasi pengguna jika diperlukan
        }
    }
}

interface UsernameAvailabilityCallback {
    fun onUsernameAvailable()
    fun onUsernameUnavailable()
}
