package com.example.moviefilteringsystem.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.MovieRepoImpl
import com.example.moviefilteringsystem.viewmodel.MovieViewModel

@Composable
fun ManageMoviesScreen() {
    val context = LocalContext.current
    val movieViewModel = remember { MovieViewModel(MovieRepoImpl()) }
    var movies by remember { mutableStateOf<List<MovieModel>>(emptyList()) }
    var selectedMovie by remember { mutableStateOf<MovieModel?>(null) }

    LaunchedEffect(Unit) {
        movieViewModel.getMovies { movies = it ?: emptyList() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (selectedMovie == null) {
            LazyColumn {
                items(movies) {
                    MovieItem(movie = it, onEdit = { selectedMovie = it }, onDelete = {
                        movieViewModel.deleteMovie(it.id) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        } else {
            EditMovie(movie = selectedMovie!!, onSave = {
                movieViewModel.updateMovie(it) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        selectedMovie = null
                    }
                }
            }, onCancel = {
                selectedMovie = null
            })
        }
    }
}

@Composable
fun MovieItem(movie: MovieModel, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title)
            Text(text = movie.description)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onEdit) { Text("Edit") }
                Button(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}

@Composable
fun EditMovie(movie: MovieModel, onSave: (MovieModel) -> Unit, onCancel: () -> Unit) {
    var title by remember { mutableStateOf(movie.title) }
    var description by remember { mutableStateOf(movie.description) }
    var imageUrl by remember { mutableStateOf(movie.imageUrl) }

    Column {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") })
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = { onSave(movie.copy(title = title, description = description, imageUrl = imageUrl)) }) { Text("Save") }
            Button(onClick = onCancel) { Text("Cancel") }
        }
    }
}
