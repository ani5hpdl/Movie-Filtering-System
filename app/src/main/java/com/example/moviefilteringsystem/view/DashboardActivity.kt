package com.example.moviefilteringsystem.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.view.ui.theme.MovieFilteringSystemTheme
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isAdminFromLogin = intent.getBooleanExtra("IS_ADMIN", false)
        enableEdgeToEdge()
        setContent {
            MovieFilteringSystemTheme {
                MainContainer(isAdminFromLogin)
            }
        }
    }
}

@Composable
fun MainContainer(isAdminFromLogin: Boolean) {
    val auth = FirebaseAuth.getInstance()
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var isQuestionnaireCompleted by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        val email = currentUser?.email
        
        // Skip questionnaire for admin
        if (email == "test@email.com") {
            isQuestionnaireCompleted = true
            return@LaunchedEffect
        }

        if (userId != null) {
            userViewModel.getUser(userId) { user ->
                isQuestionnaireCompleted = user?.isQuestionnaireCompleted ?: false
            }
        } else {
            isQuestionnaireCompleted = false
        }
    }

    if (isQuestionnaireCompleted == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFFFD700))
        }
    } else if (!isQuestionnaireCompleted!! && !isAdminFromLogin) {
        QuestionsScreen(onComplete = { isQuestionnaireCompleted = true })
    } else {
        DashboardScreen(isAdminFromLogin)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(isAdmin: Boolean) {

    data class NavItem(val label: String, val icon: ImageVector)

    var selectedIndex by remember { mutableStateOf(0) }
    var selectedMovie by remember { mutableStateOf<MovieModel?>(null) }

    val listNav = listOf(
        NavItem(label = "Home", icon = Icons.Default.Home),
        NavItem(label = "Search", icon = Icons.Default.Search),
        NavItem(label = "Library", icon = Icons.Default.VideoLibrary),
        NavItem(label = "Chat", icon = Icons.Default.Chat),
        NavItem(label = "Profile", icon = Icons.Default.Person)
    )

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            if (selectedMovie == null) {
                NavigationBar(containerColor = Color.Black) {
                    listNav.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFFFD700),
                                unselectedIconColor = Color.Gray,
                                selectedTextColor = Color(0xFFFFD700),
                                unselectedTextColor = Color.Gray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (selectedMovie == null) padding else PaddingValues(0.dp))
                .background(Color.Black)
        ) {
            if (selectedMovie != null) {
                MovieDetailsScreen(movie = selectedMovie!!, onBack = { selectedMovie = null })
            } else {
                when (selectedIndex) {
                    0 -> HomeScreen(isAdmin, onMovieClick = { selectedMovie = it })
                    1 -> SearchScreen(onMovieClick = { selectedMovie = it })
                    2 -> LibraryScreen(onMovieClick = { selectedMovie = it })
                    3 -> ChatScreen()
                    4 -> ProfileScreen()
                    else -> HomeScreen(isAdmin, onMovieClick = { selectedMovie = it })
                }
            }
        }
    }
}
