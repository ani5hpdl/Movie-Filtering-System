package com.example.moviefilteringsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.MovieRepoImpl
import com.example.moviefilteringsystem.viewmodel.MovieViewModel

@Composable
fun SearchScreen(onMovieClick: (MovieModel) -> Unit) {
    val movieViewModel = remember { MovieViewModel(MovieRepoImpl()) }
    var movies by remember { mutableStateOf<List<MovieModel>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        movieViewModel.getMovies { movies = it ?: emptyList() }
    }

    val filteredMovies = if (searchQuery.isBlank()) {
        movies
    } else {
        movies.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    val moviesByGenre = filteredMovies.groupBy { it.genre }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isBlank()) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                moviesByGenre.forEach { (genre, moviesInGenre) ->
                    MovieSection(title = "Popular in $genre", movies = moviesInGenre, onMovieClick = onMovieClick)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(filteredMovies) {
                    MovieCardItem(it, onMovieClick = onMovieClick)
                }
            }
        }
    }
}
