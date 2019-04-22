package com.fm.federico.authfirestorepoc

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_signin.*

/**
 * Created by Federico Monti.
 */
class SingInActivity : BaseActivity(), OnCompleteListener<AuthResult> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        login_button.setOnClickListener {
            auth.signInWithEmailAndPassword(
                email_edit_text.text.toString(),
                password_edit_text.text.toString()
            ).addOnCompleteListener(this)
        }

        register_button.setOnClickListener {
            when (roles_edit_text.visibility) {
                View.VISIBLE -> {
                    auth.createUserWithEmailAndPassword(
                        email_edit_text.text.toString(),
                        password_edit_text.text.toString()
                    ).addOnCompleteListener(this)
                }
                else -> {
                    roles_edit_text.visibility = View.VISIBLE
                    roles_edit_text.hint = "Insert the role"
                    Toast.makeText(baseContext, "Insert a role for the user", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            when (roles_edit_text.visibility) {
                View.VISIBLE -> {
                    currentUser?.uid?.let { uid ->
                        val role = roles_edit_text.text.toString()

                        firestore
                            .collection("roles")
                            .document(role)
                            .get()
                            .addOnSuccessListener { docSnapshot ->
                                if (docSnapshot.exists()) {
                                    docSnapshot.reference.updateArray(uid)
                                } else {
                                    docSnapshot.reference.addNewUser(uid)
                                }
                            }
                    }
                }
                else -> {
                    MainActivity.newInstance(this).let(::startActivity)
                    finish()
                }
            }
        } else {
            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun DocumentReference.addNewUser(uid: String) {
        val userMap = HashMap<String, MutableList<String>>()
        userMap["users"] = mutableListOf(uid)
        set(userMap).addCompleteListener()
    }

    private fun DocumentReference.updateArray(uid: String) {
        update("users", FieldValue.arrayUnion(uid)).addCompleteListener()
    }

    private fun Task<Void>.addCompleteListener() {
        addOnCompleteListener {
            MainActivity.newInstance(this@SingInActivity).let(::startActivity)
            finish()
        }.addOnFailureListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(baseContext, "Error saving user", Toast.LENGTH_SHORT).show()
        }
    }
}