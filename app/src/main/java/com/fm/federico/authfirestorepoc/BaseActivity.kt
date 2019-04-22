package com.fm.federico.authfirestorepoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Federico Monti.
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var auth: FirebaseAuth
    protected var firestore = FirebaseFirestore.getInstance()

    protected val currentUser: FirebaseUser?
        get() = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
}