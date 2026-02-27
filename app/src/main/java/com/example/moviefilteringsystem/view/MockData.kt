package com.example.moviefilteringsystem.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.MovieRepoImpl
import com.example.moviefilteringsystem.viewmodel.MovieViewModel

@Composable
fun UploadMockData() {
    val movieViewModel = remember { MovieViewModel(MovieRepoImpl()) }
    val context = LocalContext.current

    val mockMovies = listOf(
        MovieModel(title = "Vikram", description = "A special agent investigates a murder spree.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Vikram", rating = 8.4, genre = "Action"),
        MovieModel(title = "96", description = "Two high school sweethearts meet at a reunion.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=96", rating = 8.6, genre = "Romance"),
        MovieModel(title = "Visaranai", description = "Four immigrants are framed for a crime they didn't commit.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Visaranai", rating = 8.5, genre = "Crime"),
        MovieModel(title = "Avatar", description = "A paraplegic marine goes to an alien planet.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Avatar", rating = 7.9, genre = "Sci-Fi"),
        MovieModel(title = "Lucky Man", description = "A man gets a second chance in life after an accident.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Lucky+Man", rating = 7.8, genre = "Comedy"),
        MovieModel(title = "Kaathal - The Core", description = "A man's life is upended by a shocking revelation from his wife.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Kaathal", rating = 8.0, genre = "Drama"),
        MovieModel(title = "Leo", description = "A cafe owner's past comes back to haunt him.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Leo", rating = 7.2, genre = "Action"),
        MovieModel(title = "Jailer", description = "A retired jailer takes on a powerful crime syndicate.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Jailer", rating = 7.1, genre = "Action"),
        MovieModel(title = "Kung Fu Panda 4", description = "Po must train a new Dragon Warrior.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Kung+Fu+Panda+4", rating = 6.8, genre = "Comedy"),
        MovieModel(title = "Gorge", description = "A survival thriller about being trapped in a gorge.", imageUrl = "https://placehold.co/150x200/000000/FFFFFF/png?text=Gorge", rating = 7.5, genre = "Thriller")
    )

    LaunchedEffect(Unit) {
        mockMovies.forEach { movie ->
            movieViewModel.addMovie(movie.title, movie.description, movie.imageUrl, movie.rating, movie.genre) { success, message ->
                if (!success) {
                    Toast.makeText(context, "Failed to add ${movie.title}: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Toast.makeText(context, "Mock data upload started.", Toast.LENGTH_LONG).show()
    }
}
