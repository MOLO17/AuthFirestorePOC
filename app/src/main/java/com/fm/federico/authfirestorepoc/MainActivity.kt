package com.fm.federico.authfirestorepoc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Federico Monti.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserRoleWithQuery()
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }

    private fun getUserRoleWithQuery() {
        currentUser?.uid?.let { uid ->
            firestore
                .collection("roles")
                .whereArrayContains("users", uid)
                .get()
                .addOnCompleteListener { task ->
                    task.result?.run {
                        when (documents.size) {
                            1 -> {
                                val role = documents[0].reference.path.split("/")[1]
                                user_role_text_view.text = "Your role is: $role"
                            }
                            else -> showError()
                        }
                    }
                }
                .addOnFailureListener { showError() }
        } ?: showError()
    }

    private fun showError() {
        user_role_text_view.text = "Role not found for this user"
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, MainActivity::class.java)
    }
}
