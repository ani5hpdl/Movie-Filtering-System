package com.example.moviefilteringsystem.model

data class UserModel(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val password: String = "",
    val profileImageUrl: String = "",
    val subtitle: String = "The Movie Critic",
    val favoriteGenres: List<String> = emptyList(),
    val questionnaireResults: Map<String, String> = emptyMap(),
    val isQuestionnaireCompleted: Boolean = false
)
