package com.example.moviefilteringsystem.view

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.MovieRepoImpl
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.viewmodel.MovieViewModel
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(isAdmin: Boolean = false, onMovieClick: (MovieModel) -> Unit) {
    val movieViewModel = remember { MovieViewModel(MovieRepoImpl()) }
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val auth = FirebaseAuth.getInstance()
    
    var movies by remember { mutableStateOf<List<MovieModel>>(emptyList()) }
    var userProfile by remember { mutableStateOf<UserModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid
        movieViewModel.getMovies { allMovies ->
            movies = allMovies ?: emptyList()
            if (userId != null) {
                userViewModel.getUser(userId) { profile ->
                    userProfile = profile
                    isLoading = false
                }
            } else {
                isLoading = false
            }
        }
    }

    // Recommendation logic
    val recommendedMovies = remember(movies, userProfile) {
        val favorites = userProfile?.favoriteGenres ?: emptyList()
        if (favorites.isEmpty()) {
            emptyList()
        } else {
            movies.filter { movie ->
                favorites.any { fav -> movie.genre.contains(fav, ignoreCase = true) }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // High-End Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "SmartFlix",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Your daily dose of cinema",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }

                if (isAdmin) {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, AdminPanelActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700).copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Panel",
                            tint = Color(0xFFFFD700)
                        )
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFFD700))
                }
            } else {
                // Featured/Trending Hero
                movies.firstOrNull()?.let { 
                    SectionHeader("Trending Now")
                    TrendingHero(it, onMovieClick) 
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Personalized Recommendations
                Column {
                    SectionHeader("Recommendations For You")
                    if (recommendedMovies.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.DarkGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No movies found according to your recommendation",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(recommendedMovies) { movie ->
                                MovieCardItem(movie, onMovieClick)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                MovieSection(title = "Just Released", movies = movies.reversed(), onMovieClick = onMovieClick)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    )
}

@Composable
fun TrendingHero(movie: MovieModel, onMovieClick: (MovieModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(220.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clickable { onMovieClick(movie) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(movie.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Premium Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )
            
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = movie.genre,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun MovieSection(title: String, movies: List<MovieModel>, onMovieClick: (MovieModel) -> Unit) {
    Column {
        SectionHeader(title)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                MovieCardItem(movie, onMovieClick)
            }
        }
    }
}

@Composable
fun MovieCardItem(movie: MovieModel, onMovieClick: (MovieModel) -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onMovieClick(movie) }
    ) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(movie.imageUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.title,
            color = Color.White,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie.rating.toString(),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
