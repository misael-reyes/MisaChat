package com.example.misachat.iu.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.misachat.domain.model.LoginBody
import com.example.misachat.domain.model.UserSignIn
import com.example.misachat.domain.useCase.CreateAccountUseCase
import com.example.misachat.domain.useCase.LoginWithEmailAndPasswordUseCase
import com.example.misachat.domain.useCase.LoginWithGoogleUseCase
import com.example.misachat.iu.listOfChats.ProviderType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _showErrorDialog = MutableLiveData<String>()
    val showErrorDialog: LiveData<String> get() = _showErrorDialog

    private val _showErrorInEmail = MutableLiveData<String>()
    val showErrorInEmail: LiveData<String> get() = _showErrorInEmail

    private val _showErrorInPassword = MutableLiveData<String>()
    val showErrorInPassword: LiveData<String> get() = _showErrorInPassword

    private val _navigateToListOfChats = MutableLiveData<LoginBody>()
    val navigateToListOfChats: LiveData<LoginBody> get() = _navigateToListOfChats

    fun registerUserWithEmailAndPassword(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            viewModelScope.launch {
                createAccountUseCase(UserSignIn(email, password)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _navigateToListOfChats.value = LoginBody(email,"", ProviderType.BASIC)
                    } else {
                        _showErrorDialog.value = "No se pudo registrar al usuario"
                    }
                }
            }
        } else {
            _showErrorInEmail.value = "no puede estar en blanco"
            _showErrorInPassword.value = "no puede estar en blanco"
        }
    }

    private fun isValidEmail(email: String): Boolean {
        // por el momento solo validaremos si esta vacio, despues agregaremos mas validaciones
        return email.isNotEmpty()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty()
    }

    fun loginWithEmailAndPassword(email: String, password: String){
        if (isValidEmail(email) && isValidPassword(password)) {
            // accedemos
            viewModelScope.launch {
                loginWithEmailAndPasswordUseCase(UserSignIn(email, password)).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _navigateToListOfChats.value = LoginBody(email,"", ProviderType.BASIC)
                    } else {
                        _showErrorDialog.value = "Credenciales invalidas"
                    }
                }
            }
        } else {
            _showErrorInEmail.value = "no puede estar en blanco"
            _showErrorInPassword.value = "no puede estar en blanco"
        }
    }

    fun loginWithGoogle(requestCode: Int, resultCode: Int, data: Intent?, google_sign_in: Int) {
        if (requestCode == google_sign_in) {
            // la respuesta de esta activity corresponde con la de la autenticacion de google
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // recuperamos la cuenta
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    // recuperamos la credencial
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    viewModelScope.launch {
                        loginWithGoogleUseCase(credential).addOnCompleteListener {
                            if (it.isSuccessful) {
                                _navigateToListOfChats.value = LoginBody(account.email ?: "","", ProviderType.GOOGLE)
                            } else {
                                _showErrorDialog.value = "No se pudo autenticar el email"
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                _showErrorDialog.value = "Algo a salido mal, intente más tarde"
            }
        }
    }
}