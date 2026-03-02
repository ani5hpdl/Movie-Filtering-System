package com.example.moviefilteringsystem.viewmodel

import androidx.lifecycle.ViewModel
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepo

class UserViewModel(private val repository: UserRepo) : ViewModel() {

    fun createUser(userModel: UserModel, callback: (Boolean, String?) -> Unit) {
        repository.createUser(userModel, callback)
    }

    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        repository.login(email, password, callback)
    }

    fun getUsers(callback: (List<UserModel>?) -> Unit) {
        repository.getUsers(callback)
    }

    fun getUser(userId: String, callback: (UserModel?) -> Unit) {
        repository.getUser(userId, callback)
    }

    fun updateUser(user: UserModel, callback: (Boolean, String?) -> Unit) {
        repository.updateUser(user, callback)
    }

    fun deleteUser(userId: String, callback: (Boolean, String?) -> Unit) {
        repository.deleteUser(userId, callback)
    }
}
