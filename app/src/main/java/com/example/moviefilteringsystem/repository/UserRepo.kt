package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import javax.security.auth.callback.Callback

interface UserRepo {
    fun login(email : String,
              password : String,
              callback: (Boolean, String) -> Unit
    )

    fun register(email : String,
                 password : String,
                 callback: (Boolean, String, String) -> Unit
    )

    fun forgetPassword(email: String,
                       callback: (Boolean, String) -> Unit
    )

    fun addUserToDatabase(userId : String,
                          model: UserModel,
                          callback: (Boolean, String) -> Unit
    )

    fun getUserById(userId: String,
                    callback: (Boolean, UserModel?) -> Unit
    )

    fun getAllUser(callback: (Boolean, List<UserModel>) -> Unit
    )

    fun getcurrentUser() : FirebaseUser?

    fun deleteUser(userId: String,
                   callback: (Boolean, String) -> Unit
    )

    fun updateUser(userId: String,
                   model: UserModel,
                   callback: (Boolean, String) -> Unit
    )

    fun logout(userId: String,
               callback: (Boolean, String) -> Unit
    )
}