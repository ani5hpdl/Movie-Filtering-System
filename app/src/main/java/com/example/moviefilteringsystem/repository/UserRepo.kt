package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.UserModel

interface UserRepo {

    fun createUser(userModel: UserModel, callback: (Boolean, String?) -> Unit)
    fun login(email:String,password:String,callback: (Boolean, String?) -> Unit)
    fun getUsers(callback: (List<UserModel>?) -> Unit)
    fun getUser(userId: String, callback: (UserModel?) -> Unit)
    fun updateUser(user: UserModel, callback: (Boolean, String?) -> Unit)
    fun deleteUser(userId: String, callback: (Boolean, String?) -> Unit)
}
