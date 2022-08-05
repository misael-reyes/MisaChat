package com.example.misachat.iu.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.misachat.R
import com.example.misachat.databinding.ActivityLoginBinding
import com.example.misachat.iu.chat.ChatActivity
import com.example.misachat.iu.chat.ProviderType
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
        session()

    }

    override fun onStart() {
        super.onStart()
        binding.authLayout.visibility = View.VISIBLE
    }

    private fun session() {
        // validamos si existe una sesion activa
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            // existe sesion
            binding.authLayout.visibility = View.INVISIBLE
            showChat(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Autenticación"
        binding.btnRegister.setOnClickListener {
            // por el momento solo validamos que los textos no esten vacios
            if (binding.textInputEmail.editText?.text!!.isNotEmpty() && binding.textInputPassword.editText?.text!!.isNotEmpty()) {
                // podemos registrar a nuestro usuario
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.textInputEmail.editText?.text.toString(),
                        binding.textInputPassword.editText?.text.toString()
                    ).addOnCompleteListener {
                        // para saber si se pudo registrar o hubo un error
                        if (it.isSuccessful) {
                            // nos vamos a la sala del chat
                            showChat(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                binding.textInputEmail.error = "no puede estar en blanco"
                binding.textInputPassword.error = "no puede estar en blanco"
            }
        }

        binding.btnLogin.setOnClickListener {
            if (binding.textInputEmail.editText?.text!!.isNotEmpty() && binding.textInputPassword.editText?.text!!.isNotEmpty()) {
                // accedemos
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.textInputEmail.editText?.text.toString(),
                        binding.textInputPassword.editText?.text.toString()
                    ).addOnCompleteListener {
                        // para saber si se pudo acceder o hubo un error
                        if (it.isSuccessful) {
                            // nos vamos a la sala del chat
                            showChat(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            Toast.makeText(this, "Credenciales invalidas", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                binding.textInputEmail.error = "no puede estar en blanco"
                binding.textInputPassword.error = "no puede estar en blanco"
            }
        }
    }

    private fun showChat(email: String, provider: ProviderType) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
        finish()
    }
}