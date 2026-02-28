package com.example.moviefilteringsystem.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviefilteringsystem.model.MovieModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun LibraryScreen(onMovieClick: (MovieModel) -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("watchlist_prefs", Context.MODE_PRIVATE) }
    val gson = remember { Gson() }
    
    var watchlist by remember { mutableStateOf<List<MovieModel>>(emptyList()) }

    // Load watchlist on launch
    LaunchedEffect(Unit) {
        val json = sharedPreferences.getString("watchlist", null)
        if (json != null) {
            val type = object : TypeToken<List<MovieModel>>() {}.type
            watchlist = gson.fromJson(json, type)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "My Watchlist",
            modifier = Modifier.padding(vertical = 24.dp),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.ExtraBold
            )
        )

        if (watchlist.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Your watchlist is empty.\nStart adding some movies!",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(watchlist) { movie ->
                    MovieCardItem(movie = movie, onMovieClick = onMovieClick)
                }
            }
        }
    }
}
