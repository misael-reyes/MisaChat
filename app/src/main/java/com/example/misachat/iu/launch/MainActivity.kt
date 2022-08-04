package com.example.misachat.iu.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.misachat.R
import com.example.misachat.iu.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // colocamos el splash

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        screenSplash.setKeepOnScreenCondition { true }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // matamos esta activity para no poder regresar con el botón de regresar

        /**
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                // check if the initial data is ready
                return if(viewModel.isReady) {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    // the content is not ready; suspend
                    false
                }
            }
        )**/
    }
}