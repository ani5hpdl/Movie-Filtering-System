package com.example.moviefilteringsystem.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo: UserRepo) : ViewModel() {
    fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.login(email, password, callback)
    }

    fun register(
        email: String,
        password: String,
        callback: (Boolean,String,String) -> Unit
    ) {
        repo.register(email, password, callback)
    }

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.forgetPassword(email, callback)
    }

    fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.addUserToDatabase(userId, model, callback)
    }

    fun getUserById(
        userId: String,
        callback: (Boolean, UserModel?) -> Unit
    ) {
        repo.getUserById(userId, callback)
    }

    fun getAllUser(
        callback: (Boolean, List<UserModel>) -> Unit
    ) {
        repo.getAllUser(callback)
    }

    fun getcurrentUser(): FirebaseUser? {
        return repo.getcurrentUser()
    }

    fun deleteUser(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.deleteUser(userId, callback)
    }

    fun updateUser(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        repo.updateUser(userId, model, callback)
    }

    fun logout(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        repo.logout(userId, callback)
    }
}