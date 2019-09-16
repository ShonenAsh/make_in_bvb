package com.ashmakesstuff.makeinbvb

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView
    private val MIB_URL = "https://makeinbvb.com/"


    companion object {
        private const val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.web_view_main)
        progressBar = findViewById(R.id.progress_bar)

        webView.webViewClient = CustomWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        webView.loadUrl(MIB_URL)

        //getUserProfile()

//        val signoutButton = findViewById<Button>(R.id.signout_button)
//        signoutButton.setOnClickListener {
//            signOut()
//        }

    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        menuInflater.inflate(R.menu.menu_main_acitvity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // User clicked on a menu option in the app bar overflow menu
        return when (item?.itemId) {
            android.R.id.home -> {
                if (webView.canGoBack())
                    webView.goBack()
                else
                    Toast.makeText(this.applicationContext, "Reached the end",Toast.LENGTH_SHORT).show()
                true
            }

            // Respond to a click on the "Delete all entries" menu option
            R.id.action_logout -> {
                // Ask before logging out
                showLogoutConfirmationDialog()
                true
            }

            R.id.action_refresh -> {
                webView.reload()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    inner class CustomWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            progressBar.visibility = ProgressBar.VISIBLE
            return super.shouldOverrideUrlLoading(view, request)
        }
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            progressBar.visibility = ProgressBar.VISIBLE
            view!!.visibility = WebView.INVISIBLE
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            progressBar.visibility = View.GONE
            view!!.visibility = WebView.VISIBLE

            super.onPageFinished(view, url)
        }

    }

//    private fun getUserProfile() {
//        val user = FirebaseAuth.getInstance().currentUser
//        user?.let {
//            val username = user.displayName
//            Toast.makeText(this.applicationContext, username, Toast.LENGTH_SHORT).show()
//            val email = user.email
//
//            // Check if user's email is verified
//            val emailVerified = user.isEmailVerified
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//        }
//   }

    private fun showLogoutConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Logout") { _, id ->
            // User clicked the "Delete" button, so delete the course
            signOut()
        }
        builder.setNegativeButton("Cancel") { dialog, id ->
            // User clicked the "Cancel" button, so dismiss the dialog
            // and continue editing the pet.
            dialog?.dismiss()
        }

        // Create and show the AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.dialogBoxButton))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.dialogBoxButton))
    }

    private fun signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this.applicationContext, "Signed out!", Toast.LENGTH_SHORT).show()
        // [END auth_sign_out]
    }
}
