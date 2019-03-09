package com.fm.federico.authfirestorepoc

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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
            MainActivity.newInstance(this).let(::startActivity)
            finish()
        } else {
            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }
}