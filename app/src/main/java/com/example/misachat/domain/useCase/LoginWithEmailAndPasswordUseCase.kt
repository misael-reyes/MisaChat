package com.example.misachat.domain.useCase

import com.example.misachat.data.network.AuthenticationService
import com.example.misachat.domain.model.UserSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(userSignIn: UserSignIn): Task<AuthResult?> {
        return authenticationService.loginWithEmailAndPassword(userSignIn.email, userSignIn.password)
    }
}