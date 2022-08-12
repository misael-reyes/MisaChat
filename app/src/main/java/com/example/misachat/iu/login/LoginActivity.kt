package com.example.misachat.iu.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.misachat.R
import com.example.misachat.databinding.ActivityLoginBinding
import com.example.misachat.iu.listOfChats.ListOfChatsActivity
import com.example.misachat.iu.listOfChats.ProviderType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    // binding
    private lateinit var binding: ActivityLoginBinding

    // ViewModel
    private val viewModel: LoginViewModel by viewModels()

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

    private fun setup() {
        title = "Autenticación"
        initListeners()
        initObservers()
    }

    private fun session() {
        // validamos si existe una sesion activa
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            // existe sesion
            binding.authLayout.visibility = View.INVISIBLE
            showListOfChats(email, ProviderType.valueOf(provider))
        }
    }

    private fun initListeners() {

        binding.btnRegister.setOnClickListener {
            viewModel.registerUserWithEmailAndPassword(
                binding.textInputEmail.editText?.text.toString(),
                binding.textInputPassword.editText?.text.toString()
            )
        }

        binding.btnLogin.setOnClickListener {
            viewModel.loginWithEmailAndPassword(
                binding.textInputEmail.editText?.text.toString(),
                binding.textInputPassword.editText?.text.toString()
            )
        }

        binding.btnGoogle.setOnClickListener {

            // Configuración Google Sign In

            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            // Cliente de autenticación de google

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    private fun initObservers() {
        viewModel.showErrorDialog.observe(this) { message ->
            showAlert(message)
        }

        viewModel.showErrorInEmail.observe(this) {
            binding.textInputEmail.error = it
        }

        viewModel.showErrorInPassword.observe(this) {
            binding.textInputPassword.error = it
        }

        viewModel.navigateToListOfChats.observe(this) {
            showListOfChats(it.email, it.provider)
        }
    }

    private fun showListOfChats(email: String, provider: ProviderType) {
        val intent = Intent(this, ListOfChatsActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // resultado retornado de lanzar el intent de GoogleSignInApi.getSignInIntent()
        viewModel.loginWithGoogle(requestCode, resultCode, data, GOOGLE_SIGN_IN)
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}