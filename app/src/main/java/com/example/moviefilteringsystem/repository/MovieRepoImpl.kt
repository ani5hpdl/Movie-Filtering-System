package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.MovieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MovieRepoImpl : MovieRepo {
    private val database = FirebaseDatabase.getInstance().getReference("Movies")

    override fun addMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit) {
        val movieId = database.push().key ?: ""
        val newMovie = movie.copy(id = movieId)
        database.child(movieId).setValue(newMovie)
            .addOnSuccessListener {
                callback(true, "Movie added successfully")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }

    override fun getMovies(callback: (List<MovieModel>?) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movies = snapshot.children.mapNotNull { it.getValue(MovieModel::class.java) }
                callback(movies)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    override fun getMovie(movieId: String, callback: (MovieModel?) -> Unit) {
        database.child(movieId).get().addOnSuccessListener {
            callback(it.getValue(MovieModel::class.java))
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun updateMovie(movie: MovieModel, callback: (Boolean, String?) -> Unit) {
        database.child(movie.id).setValue(movie)
            .addOnSuccessListener {
                callback(true, "Movie updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }

    override fun deleteMovie(movieId: String, callback: (Boolean, String?) -> Unit) {
        database.child(movieId).removeValue()
            .addOnSuccessListener {
                callback(true, "Movie deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }
}
