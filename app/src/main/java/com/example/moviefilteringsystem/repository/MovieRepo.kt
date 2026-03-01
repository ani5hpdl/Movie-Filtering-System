package com.example.moviefilteringsystem.repository

import android.content.Context
import android.net.Uri
import com.example.moviefilteringsystem.model.MovieModel

interface MovieRepo {
    fun addMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit)
    fun getMovies(callback: (List<MovieModel>?) -> Unit)
    fun getMovie(movieId: String, callback: (MovieModel?) -> Unit)
    fun updateMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit)
    fun deleteMovie(movieId: String, callback: (Boolean, String?) -> Unit)
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)
    fun getFileNameFromUri(context: Context, uri: Uri): String?
}
