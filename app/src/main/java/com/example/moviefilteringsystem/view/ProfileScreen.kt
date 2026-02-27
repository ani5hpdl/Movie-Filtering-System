package com.example.moviefilteringsystem.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    var user by remember { mutableStateOf<UserModel?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        currentUser?.let {
            userViewModel.getUser(it.uid) { fetchedUser ->
                user = fetchedUser
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        user?.let {
            ProfileHeader(it) { showEditDialog = true }
            Spacer(modifier = Modifier.height(32.dp))
            ProfileMenuList()
        }
    }

    if (showEditDialog) {
        user?.let {
            EditProfileDialog(user = it, onDismiss = { showEditDialog = false }, onSave = {
                userViewModel.updateUser(it) { success, message ->
                    if (success) {
                        user = it
                        showEditDialog = false
                    }
                    // Show toast or handle error
                }
            })
        }
    }
}

@Composable
fun ProfileHeader(user: UserModel, onEditClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(user.profileImageUrl.ifEmpty { "https://placehold.co/100x100/000000/FFFFFF/png?text=User" }),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(user.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(user.subtitle, color = Color.Gray, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
        }
    }
}

@Composable
fun ProfileMenuList() {
    Column {
        ProfileMenuItem(Icons.Default.Visibility, "Recently Viewed")
        ProfileMenuItem(Icons.Default.Save, "Saved for Later")
        ProfileMenuItem(Icons.Default.ThumbUp, "Likes")
        ProfileMenuItem(Icons.Default.Movie, "Reviewed")
        ProfileMenuItem(Icons.Default.Star, "Rated")
        ProfileMenuItem(Icons.Default.Favorite, "Favourite Theatres")
        ProfileMenuItem(Icons.Default.Notifications, "Notification")
        ProfileMenuItem(Icons.Default.Videocam, "Video Quality")
        ProfileMenuItem(Icons.Default.Theaters, "Watch Preference")
        ProfileMenuItem(Icons.Default.Settings, "Settings")
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { /* Handle item click */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.Yellow)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, color = Color.White, fontSize = 18.sp)
    }
}

@Composable
fun EditProfileDialog(user: UserModel, onDismiss: () -> Unit, onSave: (UserModel) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var subtitle by remember { mutableStateOf(user.subtitle) }
    var profileImageUrl by remember { mutableStateOf(user.profileImageUrl) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtitle") })
                OutlinedTextField(value = profileImageUrl, onValueChange = { profileImageUrl = it }, label = { Text("Profile Image URL") })
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedUser = user.copy(name = name, subtitle = subtitle, profileImageUrl = profileImageUrl)
                onSave(updatedUser)
                Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
