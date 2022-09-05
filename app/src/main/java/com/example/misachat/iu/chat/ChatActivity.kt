package com.example.misachat.iu.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.misachat.R
import com.example.misachat.databinding.ActivityChatBinding
import com.example.misachat.domain.model.Message
import com.example.misachat.domain.model.Token
import com.example.misachat.iu.adapters.MessageRecyclerViewAdapter
import com.example.misachat.iu.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.util.*

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
            notifyUser(message)
            binding.messageTextField.setText("")
        }
    }

    private fun notifyUser(message: Message) {
        /**
         * lo que hacemos aqui es hacer una peticion http con volley mandando el objeto json que nos indica la
         * documentacion, para mas informacion consulta:
         *
         * https://firebase.google.com/docs/cloud-messaging/send-message#send-messages-using-the-legacy-app-server-protocols
         */
        val myRequest: RequestQueue = Volley.newRequestQueue(this)
        val json: JSONObject = JSONObject()
        var tokens : List<Token> = emptyList()

        try {
            val token = Firebase.firestore.collection("users").document(user).collection("tokens").get().addOnSuccessListener {
                tokens = it.toObjects(Token::class.java)

                val notification: JSONObject = JSONObject()
                notification.put("title", message.from)
                notification.put("detail", message.message)
                json.put("data", notification)
                json.put("to", tokens[0].token)

                val URL = "https://fcm.googleapis.com/fcm/send"
                val request: JsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.POST, URL, json,
                        { response ->
                            Log.i("prueba", response.toString())
                        },
                        { error ->
                            Log.i("prueba", "hubo un error " + error.message.toString())
                        }
                    ) {
                        override fun getHeaders(): Map<String, String> {
                            val headers = HashMap<String, String>()
                            headers.put("Content-Type", "application/json")
                            headers.put(
                                "Authorization",
                                "key=AAAA6VFdRdo:APA91bGgFq1sZz90JlFNGzn6os2RyAKZPA2po-jdFNRfSyYnzK0cQ6iiyp1mAyyXyYf-0lfKTE_lzgVyOa-Ka-zZfwLji1Hkuumqrlxj8Iwq6-0SHB9AuSF5k2IL2RtKEWv8Ua52S-dL"
                            )
                            return headers
                        }
                    }
                myRequest.add(request)
            }
        } catch (e: JSONException) {
            Log.i("prueba", e.message.toString())
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