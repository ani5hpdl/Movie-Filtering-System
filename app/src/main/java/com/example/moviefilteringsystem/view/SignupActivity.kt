package com.example.moviefilteringsystem.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviefilteringsystem.R
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.view.ui.theme.MovieFilteringSystemTheme
import com.example.moviefilteringsystem.viewmodel.UserViewModel

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieFilteringSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    SignupScreen()
                }
            }
        }
    }
}

@Composable
fun SignupScreen() {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(false) }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- App Logo ---
        Image(
            painter = painterResource(R.drawable.smartflixlogo),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Header Text ---
        Text(
            text = "Create Your Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Join the movie-loving community",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Full Name Text Field ---
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.AccountCircle, "Full Name Icon") },
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Email Text Field ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, "Email Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Contact Number Text Field ---
        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Contact Number") },
            leadingIcon = { Icon(Icons.Default.Phone, "Contact Number Icon") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, "Password Icon") },
            trailingIcon = {
                IconButton(onClick = {
                    visibility = !visibility
                }) {
                    Icon(
                        painter = if (visibility)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),

                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = customTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty() && contactNumber.isNotEmpty()) {
                    userViewModel.register(email, password) { success, message, userId ->
                        if (success && userId != null) {
                            val model = UserModel(
                                userId = userId,
                                fullName = fullName,
                                email = email,
                                contactNumber = contactNumber,
                                password = password
                            )

                            userViewModel.addUserToDatabase(userId, model) { dbSuccess, dbMessage ->
                                if (dbSuccess) {
                                    Toast.makeText(context, dbMessage, Toast.LENGTH_LONG).show()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, dbMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },


            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(
                "Or sign up with",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            SocialLoginButton(iconId = R.drawable.gmail, text = "Google") {  }
            Spacer(modifier = Modifier.width(16.dp))
            SocialLoginButton(iconId = R.drawable.facebbook, text = "Facebook") {  }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Login Prompt ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Log In",
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Navigate to Log In", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    iconId: Int,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = "$text logo",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}

@Composable
fun customTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = Color.Gray,
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
    unfocusedLeadingIconColor = Color.Gray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)

@Preview
@Composable
fun SignupPreview() {
    MovieFilteringSystemTheme {
        SignupScreen()
    }
}
