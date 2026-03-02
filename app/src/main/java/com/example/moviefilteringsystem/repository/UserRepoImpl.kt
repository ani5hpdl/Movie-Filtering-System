package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepoImpl:UserRepo {

    val firebaseAuth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("Users")


    override fun createUser(userModel: UserModel, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(userModel.email,userModel.password)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    val firebaseUser = firebaseAuth.currentUser
                    val uid = firebaseUser!!.uid
                    userModel.id = uid
                    database.child(uid).setValue(userModel)
                        .addOnSuccessListener {
                            callback(true,"User created successfully")
                        }
                        .addOnFailureListener {
                            callback(false,it.message)
                        }
                }else{
                    callback(false,it.exception?.message)
                }
            }
    }

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    callback(true,"Login successful")
                }else{
                    callback(false,it.exception?.message)
                }
            }
    }

    override fun getUsers(callback: (List<UserModel>?) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(UserModel::class.java) }
                callback(users)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

    override fun getUser(userId: String, callback: (UserModel?) -> Unit) {
        database.child(userId).get().addOnSuccessListener {
            callback(it.getValue(UserModel::class.java))
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun updateUser(user: UserModel, callback: (Boolean, String?) -> Unit) {
        database.child(user.id).setValue(user)
            .addOnSuccessListener {
                callback(true, "User updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }

    override fun deleteUser(userId: String, callback: (Boolean, String?) -> Unit) {
        database.child(userId).removeValue()
            .addOnSuccessListener {
                callback(true, "User deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message)
            }
    }
}
