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
        MovieModel(
            title = "Vikram",
            description = "A special agent investigates a murder spree committed by a masked group of serial killers.",
            imageUrl = "https://imgs.search.brave.com/uyARm9AIHJhOELmRgQHcOCbFlFAjd6Z99-RQFOLvahQ/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJhY2Nlc3Mu/Y29tL2Z1bGwvODI0/MjU0OS5qcGc",
            rating = 8.4,
            genre = "Action"
        ),
        MovieModel(
            title = "Leo",
            description = "A gentle cafe owner becomes a local hero through an act of violence, which brings forth ghosts from his past.",
            imageUrl = "https://imgs.search.brave.com/FXMx08c-vWjUAqm8Hif7o7VAjQKgVI6h5RI0IcKjGhA/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzLzI5LzYz/L2NmLzI5NjNjZmMy/MTEwMjg1YzQyOWE1/ZmNlNGVjNWRmZGEy/LmpwZw",
            rating = 7.2,
            genre = "Action"
        ),
        MovieModel(
            title = "Jailer",
            description = "A retired jailer goes on a hunt to find his son's killers, only to find himself in the middle of a massive conspiracy.",
            imageUrl = "https://imgs.search.brave.com/jZIXKap23LYKrxqXLMnQ3ihONv-BJWBaZEEOrpL9e4Q/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzL2JmLzM0/L2U5L2JmMzRlOTIz/YzdjN2VkMTZlYzM3/N2RkZGQ4YzliYWUy/LmpwZw",
            rating = 7.1,
            genre = "Action"
        ),
        MovieModel(
            title = "96",
            description = "Two high school sweethearts meet at a reunion after 22 years and reminisce about their past.",
            imageUrl = "https://imgs.search.brave.com/LUwcJzHCCUrdb1zh00WIu3RMTLwIhWwvGvhiSl-Hmy0/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9taXIt/czMtY2RuLWNmLmJl/aGFuY2UubmV0L3By/b2plY3RzLzQwNC81/YmM4NTgyMDA3MDIw/NTUuWTNKdmNDdzFO/REF3TERReU1qTXNN/Q3d4TWpZNS5wbmc",
            rating = 8.6,
            genre = "Romance"
        ),
        MovieModel(
            title = "Visaranai",
            description = "Four immigrants are tortured by police to confess to a crime they didn't commit.",
            imageUrl = "https://imgs.search.brave.com/3Yx55ZKWvXphg6opFPGjfpb358kquAI1Eq2ZyGKqR4o/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly90aGVy/ZXZpZXdtb25rLmNv/bS9hc3NldHMvbWVk/aWEvbW92aWVzL3Bv/c3RlcnMvdzMwMC84/M2I2MDk4MjA5NmRl/OTJhNWQ5ZTg2OTgx/NTNkMDAyNy5qcGc",
            rating = 8.5,
            genre = "Crime"
        ),
        MovieModel(
            title = "The Dark Knight",
            description = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            imageUrl = "https://imgs.search.brave.com/u3qrKZdhDI8xwIx6d647_S3v-Ti4821Jg0S5za4Pcn8/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzL2I3L2M4/LzFhL2I3YzgxYWFm/NTY3MjE2YmIyNDll/OTk0ZDBkZjAzYzQ1/LmpwZw",
            rating = 9.0,
            genre = "Action"
        ),
        MovieModel(
            title = "Inception",
            description = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            imageUrl = "https://imgs.search.brave.com/u3qrKZdhDI8xwIx6d647_S3v-Ti4821Jg0S5za4Pcn8/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzL2I3L2M4/LzFhL2I3YzgxYWFm/NTY3MjE2YmIyNDll/OTk0ZDBkZjAzYzQ1/LmpwZw",
            rating = 8.8,
            genre = "Sci-Fi"
        ),
        MovieModel(
            title = "Interstellar",
            description = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.",
            imageUrl = "https://imgs.search.brave.com/u3qrKZdhDI8xwIx6d647_S3v-Ti4821Jg0S5za4Pcn8/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9pLnBp/bmltZy5jb20vb3Jp/Z2luYWxzL2I3L2M4/LzFhL2I3YzgxYWFm/NTY3MjE2YmIyNDll/OTk0ZDBkZjAzYzQ1/LmpwZw",
            rating = 8.7,
            genre = "Sci-Fi"
        ),
        MovieModel(
            title = "The Hangover",
            description = "Three buddies wake up from a bachelor party in Las Vegas, with no memory of the previous night and the bachelor missing.",
            imageUrl = "https://imgs.search.brave.com/BFpF8MKQ7WW4MGjn070JsNptG8g1jVA61ZK3UbPbYiY/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly93YWxs/cGFwZXJzLmNvbS9p/bWFnZXMvaGQvdGhl/LWhhbmdvdmVyLW1v/dmllLXBvc3Rlci1k/dmQtY292ZXItcGF6/Nnk1MW45bXRzZW0y/Yy5qcGc",
            rating = 7.7,
            genre = "Comedy"
        ),
        MovieModel(
            title = "Parasite",
            description = "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
            imageUrl = "https://imgs.search.brave.com/s6x35_TIkHBkLTecSkdyNyuxVEMEy10CV8qxlzex_r8/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9tLm1l/ZGlhLWFtYXpvbi5j/b20vaW1hZ2VzL0kv/OTFLQXJZUDAzWUwu/anBn",
            rating = 8.5,
            genre = "Thriller"
        ),
        MovieModel(
            title = "Dune: Part Two",
            description = "Paul Atreides unites with Chani and the Fremen while on a warpath of revenge against the conspirators who destroyed his family.",
            imageUrl = "https://imgs.search.brave.com/9-YIjodwOIGZe1JD6FHHQrtz-PGLqtDgYQSzf5myJG0/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9tLm1l/ZGlhLWFtYXpvbi5j/b20vaW1hZ2VzL0kv/NTFQcEI4ZUFQakwu/anBn",
            rating = 8.6,
            genre = "Sci-Fi"
        ),
        MovieModel(
            title = "The Godfather",
            description = "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            imageUrl = "https://imgs.search.brave.com/bQ4FgNSygdhqc19RRlo3FvWRPdzf3-W6SAAUJXI-Bqw/rs:fit:500:0:1:0/g:ce/aHR0cHM6Ly9tLm1l/ZGlhLWFtYXpvbi5j/b20vaW1hZ2VzL0kv/NDFiVjdXMjdtV0wu/anBn",
            rating = 9.2,
            genre = "Drama"
        )
    )

    LaunchedEffect(Unit) {
        mockMovies.forEach { movie ->
            movieViewModel.addMovie(movie.title, movie.description, movie.imageUrl, movie.rating, movie.genre) { success, message ->
                if (!success) {
                    Toast.makeText(context, "Failed to add ${movie.title}: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Toast.makeText(context, "Updating database with real posters...", Toast.LENGTH_LONG).show()
    }
}
