package com.example.moviefilteringsystem.model

data class UserModel(
    var userId : String,
    var fullName: String,
    var email: String,
    var contactNumber: String,
    var password: String
){
    fun toMap(): Map<String, Any>
    {
        return mapOf(
            "userId" to userId,
            "fullName" to fullName,
            "email" to email,
            "contactNumber" to contactNumber,
            "password" to password
        )

    }
}