package com.example.moviefilteringsystem.model

data class MovieModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val genre: String = ""
)
