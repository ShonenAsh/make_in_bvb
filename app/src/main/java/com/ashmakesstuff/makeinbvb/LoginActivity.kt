package com.ashmakesstuff.makeinbvb

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 123
    }

    private lateinit var firebaseAuth: FirebaseAuth
    //private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInButton = findViewById<Button>(R.id.login_button)
        signInButton.setOnClickListener {
            createSignInIntent()
        }

        //hide action bar
        val aB = supportActionBar!!
        aB.hide()

//        mAuth = FirebaseAuth.getInstance()
//        // Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()

        firebaseAuth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createSignInIntent() {
        val loginProviders = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

        //Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(loginProviders)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Login Successful
                val user = FirebaseAuth.getInstance().currentUser

                Toast.makeText(this.applicationContext, "Login success!", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this.applicationContext, "Login Failed", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

//    private fun delete() {
//        // [START auth_fui_delete]
//        AuthUI.getInstance()
//            .delete(this)
//            .addOnCompleteListener {
//                // ...
//            }
//        // [END auth_fui_delete]
//    }
}
