package com.example.misachat.data.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClient @Inject constructor() {

    // variable para checar lo relacionado a la autenticacion en firebase
    val auth: FirebaseAuth get() = FirebaseAuth.getInstance()

    // variable para checar lo relacionado con firestore
    val db = Firebase.firestore
}