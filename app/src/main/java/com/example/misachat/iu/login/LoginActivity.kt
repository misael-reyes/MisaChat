package com.example.misachat.iu.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.misachat.R
import com.example.misachat.databinding.ActivityLoginBinding
import com.example.misachat.iu.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup
        setup()
    }

    private fun setup() {
        title = "Autenticación"
        binding.btnRegister.setOnClickListener {
            // por el momento solo validamos que los textos no esten vacios
            if (binding.textInputEmail.editText?.text!!.isNotEmpty() && binding.textInputPassword.editText?.text!!.isNotEmpty()) {
                // podemos registrar a nuestro usuario
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(binding.textInputEmail.editText?.text.toString(),
                        binding.textInputPassword.editText?.text.toString()).addOnCompleteListener {
                            // para saber si se pudo registrar o hubo un error
                            if (it.isSuccessful) {
                                // nos vamos a la sala del chat
                                    // si queremos acceder a el email por ejejmplo, hacemos esto
                                        //it.result?.user?.email ?: ""
                                showChat()
                            } else {
                                Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show()
                            }
                    }
            } else {
                binding.textInputEmail.error = "pendejo, rellenalo"
                binding.textInputPassword.error = "pendejo, rellenalo"
            }
        }

        binding.btnLogin.setOnClickListener {
            if (binding.textInputEmail.editText?.text!!.isNotEmpty() && binding.textInputPassword.editText?.text!!.isNotEmpty()) {
                // accedemos
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(binding.textInputEmail.editText?.text.toString(),
                        binding.textInputPassword.editText?.text.toString()).addOnCompleteListener {
                        // para saber si se pudo acceder o hubo un error
                        if (it.isSuccessful) {
                            // nos vamos a la sala del chat
                            // si queremos acceder a el email por ejemplo, hacemos esto
                            //it.result?.user?.email ?: ""
                            showChat()
                        } else {
                            Toast.makeText(this, "Credenciales invalidas", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                binding.textInputEmail.error = "pendejo, rellenalo"
                binding.textInputPassword.error = "pendejo, rellenalo"
            }
        }
    }

    private fun showChat() {
        val intent = Intent(this, ChatActivity::class.java).apply {
            // podremos hacer esto para pasar parametros a la nueva activity
            putExtra("", "")
        }
        startActivity(intent)
        finish()
    }
}