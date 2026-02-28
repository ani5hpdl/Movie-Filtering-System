package com.example.moviefilteringsystem.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.MovieRepo

class MovieViewModel(val repo: MovieRepo) : ViewModel() {

    fun addMovie(title: String, description: String, imageUrl: String, rating: Double, genre: String, callback: (Boolean, String?) -> Unit) {
        val movie = MovieModel(title = title, description = description, imageUrl = imageUrl, rating = rating, genre = genre)
        repo.addMovie(movie, callback)
    }

    fun updateMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit) {
        repo.updateMovie(movie, callback)
    }

    fun deleteMovie(movieId: String, callback: (Boolean, String?) -> Unit) {
        repo.deleteMovie(movieId, callback)
    }

    private val _movies = MutableLiveData<MovieModel?>()
    val movies : MutableLiveData<MovieModel?> get() = _movies

    private val _allMovies = MutableLiveData<List<MovieModel>?>()
    val allMovies : MutableLiveData<List<MovieModel>?> get() = _allMovies

    private val _loading = MutableLiveData<Boolean>()
    val loading : MutableLiveData<Boolean> get() = _loading

    fun getMovies(callback: (List<MovieModel>?) -> Unit) {
        repo.getMovies(callback)
    }

    fun getMovie(movieId: String, callback: (MovieModel?) -> Unit) {
        repo.getMovie(movieId, callback)
    }

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }
}
