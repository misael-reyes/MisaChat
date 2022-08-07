package com.example.misachat.domain.useCase

import com.example.misachat.data.network.AuthenticationService
import com.example.misachat.domain.model.LoginBody
import com.example.misachat.iu.listOfChats.ProviderType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(credential: AuthCredential): Task<AuthResult?> {
        return authenticationService.loginWithGoogle(credential)
    }
}