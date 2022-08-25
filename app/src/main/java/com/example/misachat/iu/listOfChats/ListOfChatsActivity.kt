package com.example.misachat.iu.listOfChats

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misachat.R
import com.example.misachat.databinding.ActivityListOfChatsBinding
import com.example.misachat.domain.model.Chat
import com.example.misachat.iu.adapters.ChatRecyclerViewAdapter
import com.example.misachat.iu.chat.ChatActivity
import com.example.misachat.iu.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


enum class ProviderType {
    BASIC,
    GOOGLE
}

@AndroidEntryPoint
class ListOfChatsActivity : AppCompatActivity() {

    // binding

    private lateinit var binding: ActivityListOfChatsBinding

    // viewmodel

    private val viewModel: ListOfChatsViewModel by viewModels()

    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfChatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup

        title = "Lista de chats"
        val bundle = intent.extras
        email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        // Guardado de datos

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        setup()
    }

    private fun setup() {
        initObservers()
        initListeners()
        initViews()
    }

    private fun initViews() {
        if (email != null) {
            viewModel.getAllChatsUser(email!!)
        }
    }

    private fun initListeners() {
        binding.btnChat.setOnClickListener { newChat() }
    }

    private fun newChat() {
        viewModel.newChat(email ?: "", binding.etEmailChat.text.toString())
    }

    private fun initObservers() {
        viewModel.chats.observe(this) {
            initRecyclerView(it)
        }

        viewModel.isAdded.observe(this) {
            navigateToChat(binding.etEmailChat.text.toString(), it.id)
        }

        viewModel.errorAdded.observe(this) {
            if (!it) Toast.makeText(this, "No puede dejar el campo vacio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToChat(email: String, chatId: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", email)
        startActivity(intent)
    }

    private fun initRecyclerView(chats: List<Chat>) {
        val manager = LinearLayoutManager(this)
        binding.listChatsRecyclerView.layoutManager = manager
        binding.listChatsRecyclerView.adapter =
            ChatRecyclerViewAdapter(chats) {
                onItemSelected(it)
            }
    }

    private fun onItemSelected(chat: Chat) {
        navigateToChat(email ?: "", chat.id)
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

                // borrar datos

                val prefs = getSharedPreferences(
                    getString(R.string.prefs_file),
                    Context.MODE_PRIVATE
                ).edit()
                prefs.clear()
                prefs.apply()

                // cerramos sesion

                FirebaseAuth.getInstance().signOut()
                //onBackPressed() // regresamos a la ventana anterior
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}