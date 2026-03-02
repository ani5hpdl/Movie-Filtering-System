package com.example.moviefilteringsystem.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.moviefilteringsystem.model.MovieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class MovieRepoImpl : MovieRepo {
    private val database = FirebaseDatabase.getInstance().getReference("Movies")

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dusnktkk7",
            "api_key" to "597162626537592",
            "api_secret" to "QCNxWNXYobaiZ9aO9Nm6AoPG-ME"
        )
    )

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

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_movie_image"

                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                var imageUrl = response["url"] as String?
                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}
