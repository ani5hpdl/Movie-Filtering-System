package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.MovieModel

interface MovieRepo {
    fun addMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit)
    fun getMovies(callback: (List<MovieModel>?) -> Unit)
    fun getMovie(movieId: String, callback: (MovieModel?) -> Unit)
    fun updateMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit)
    fun deleteMovie(movieId: String, callback: (Boolean, String?) -> Unit)
}
