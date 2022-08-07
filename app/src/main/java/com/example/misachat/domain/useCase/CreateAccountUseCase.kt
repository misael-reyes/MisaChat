package com.example.misachat.domain.useCase

import com.google.android.gms.tasks.Task
import com.example.misachat.data.network.AuthenticationService
import com.example.misachat.domain.model.UserSignIn
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(userSignIn: UserSignIn): Task<AuthResult?> {
        /*
        var accountCreated = false
        authenticationService.registerUserWithEmailAndPassword(userSignIn.email, userSignIn.password).addOnCompleteListener {
            accountCreated = it.isSuccessful
        }
        return accountCreated*/
        return authenticationService.registerUserWithEmailAndPassword(userSignIn.email, userSignIn.password)

    }
}