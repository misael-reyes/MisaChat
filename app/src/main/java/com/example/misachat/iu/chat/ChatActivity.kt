package com.example.misachat.iu.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.misachat.R
import com.google.firebase.auth.FirebaseAuth

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    // esto es para inflar el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // gestionamos los eventos de los clic del menj
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_logout -> {
                // cerramos sesion
                FirebaseAuth.getInstance().signOut()
                onBackPressed() // regresamos a la ventana anterior
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}