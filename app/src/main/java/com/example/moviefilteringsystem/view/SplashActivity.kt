package com.example.moviefilteringsystem.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviefilteringsystem.R
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreenContent()
        }
    }
}

@Composable
fun SplashScreenContent() {
    val context = LocalContext.current
    val activity = context as Activity
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isRemembered = sharedPref.getBoolean("remembered", false)
        val savedEmail = sharedPref.getString("email", null)
        val savedPassword = sharedPref.getString("password", null)

        delay(2000) // Professional delay for branding

        if (isRemembered && savedEmail != null && savedPassword != null) {
            userViewModel.login(savedEmail, savedPassword) { success: Boolean, _: String? ->
                if (success) {
                    val isAdmin = savedEmail == "test@email.com"
                    val intent = Intent(context, DashboardActivity::class.java).apply {
                        putExtra("IS_ADMIN", isAdmin)
                    }
                    context.startActivity(intent)
                } else {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }
                activity.finish()
            }
        } else {
            context.startActivity(Intent(context, LoginActivity::class.java))
            activity.finish()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.smartflixlogo),
                contentDescription = "SmartFlix Logo",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color(0xFFFFD700),
                strokeWidth = 3.dp
            )
        }
    }
}

@Preview
@Composable
fun SplashPreview() {
    SplashScreenContent()
}
