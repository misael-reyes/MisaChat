package com.example.misachat.iu.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.misachat.R
import com.example.misachat.databinding.ActivityChatBinding
import com.example.misachat.domain.model.Message
import com.example.misachat.iu.adapters.MessageRecyclerViewAdapter
import com.example.misachat.iu.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private var chatId = ""
    private var user = ""

    // binding

    private lateinit var binding: ActivityChatBinding

    // viewModel

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }
        title = "Chat con $user"
        initObservers()
        initListeners()
        if (chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews() {
        viewModel.getMessages(chatId)
    }

    private fun initListeners() {
        binding.sendMessageButton.setOnClickListener {
            val message = Message(
                message = binding.messageTextField.text.toString(),
                from = user
            )
            viewModel.sendMessage(message, chatId)
            binding.messageTextField.setText("")
        }
    }

    private fun initObservers() {
        viewModel.messages.observe(this) {
            initRecyclerView(it)
        }
    }

    private fun initRecyclerView(messages: List<Message>) {
        val manager = LinearLayoutManager(this)
        binding.messagesRecylerView.layoutManager = manager
        binding.messagesRecylerView.adapter =
            MessageRecyclerViewAdapter(messages, user)
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

                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()

                // cerramos sesion

                FirebaseAuth.getInstance().signOut()
                //onBackPressed() // regresamos a la ventana anterior
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}