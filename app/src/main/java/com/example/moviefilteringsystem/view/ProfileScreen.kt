package com.example.moviefilteringsystem.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddPhotoAlternate
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.MovieRepoImpl
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.view.components.smartFlixTextFieldColors
import com.example.moviefilteringsystem.viewmodel.MovieViewModel
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val movieViewModel = remember { MovieViewModel(MovieRepoImpl()) }
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var user by remember { mutableStateOf<UserModel?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            userViewModel.getUser(it.uid) { fetchedUser ->
                user = fetchedUser
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp)
        ) {
            user?.let {
                ProfileHeader(it) { showEditDialog = true }
                Spacer(modifier = Modifier.height(24.dp))
                ProfileMenuList(onLogout = {
                    auth.signOut()
                    // Clear remember me prefs
                    val sharedPref = context.getSharedPreferences("LoginPrefs", android.content.Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    activity.finish()
                })
            }
        }
    }

    if (showEditDialog) {
        user?.let {
            EditProfileDialog(
                user = it,
                onDismiss = { showEditDialog = false },
                onSave = { updatedUser, selectedUri ->
                    if (selectedUri != null) {
                        movieViewModel.uploadImage(context, selectedUri) { imageUrl ->
                            if (imageUrl != null) {
                                val finalUser = updatedUser.copy(profileImageUrl = imageUrl)
                                userViewModel.updateUser(finalUser) { success, _ ->
                                    if (success) {
                                        user = finalUser
                                        showEditDialog = false
                                    }
                                }
                            }
                        }
                    } else {
                        userViewModel.updateUser(updatedUser) { success, _ ->
                            if (success) {
                                user = updatedUser
                                showEditDialog = false
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileHeader(user: UserModel, onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFD700).copy(alpha = 0.15f), Color.Black)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = rememberAsyncImagePainter(user.profileImageUrl.ifEmpty { "https://placehold.co/200x200/000000/FFFFFF/png?text=User" }),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .shadow(8.dp, CircleShape),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = user.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = user.subtitle,
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileMenuList(onLogout: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "My Activity",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        ProfileMenuItem(Icons.Default.Visibility, "Recently Viewed")
        ProfileMenuItem(Icons.Default.Save, "Saved for Later")
        ProfileMenuItem(Icons.Default.ThumbUp, "Likes")
        ProfileMenuItem(Icons.Default.Movie, "Reviewed")
        ProfileMenuItem(Icons.Default.Star, "Rated")
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray, thickness = 0.5.dp)
        
        Text(
            text = "Preferences",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        ProfileMenuItem(Icons.Default.Favorite, "Favourite Theatres")
        ProfileMenuItem(Icons.Default.Notifications, "Notification")
        ProfileMenuItem(Icons.Default.Videocam, "Video Quality")
        ProfileMenuItem(Icons.Default.Theaters, "Watch Preference")
        ProfileMenuItem(Icons.Default.Settings, "Settings")

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.DarkGray, thickness = 0.5.dp)

        Text(
            text = "Account",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        ProfileMenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            text = "Log Out",
            textColor = Color.Red,
            onClick = onLogout
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    text: String,
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (textColor == Color.Red) Color.Red else Color(0xFFFFD700),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun EditProfileDialog(user: UserModel, onDismiss: () -> Unit, onSave: (UserModel, Uri?) -> Unit) {
    var name by remember { mutableStateOf(user.name) }
    var subtitle by remember { mutableStateOf(user.subtitle) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        title = {
            Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image Picker
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(user.profileImageUrl.ifEmpty { "https://placehold.co/100x100/000000/FFFFFF/png?text=+" }),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White)
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    colors = smartFlixTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtitle") },
                    colors = smartFlixTextFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isUploading = true
                    onSave(user.copy(name = name, subtitle = subtitle), selectedImageUri)
                },
                enabled = !isUploading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isUploading) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}
