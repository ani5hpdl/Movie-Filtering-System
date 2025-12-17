package com.example.moviefilteringsystem.repository

import com.example.moviefilteringsystem.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.toMap

class UserRepoImpl : UserRepo {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref : DatabaseReference =database.getReference("Users")

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"User Login SucessFullyy")
                }else{
                    callback(false,"${it.exception?.message}")
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"User Registered SucessFullyy","${auth.currentUser?.uid}")
                }else{
                    callback(false,"${it.exception?.message}","")
                }
            }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    callback(true,"Email Sent SucessFullyy")
                }else{
                    callback(false,"${it.exception?.message}")
                }
            }
    }

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        var id = ref.push().key.toString()
        model.userId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"User Added Sucesssfully")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun getUserById(
        userId: String,
        callback: (Boolean, UserModel?) -> Unit
    ) {
        ref.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)
                    if(user != null){
                        callback(true,user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,null)
            }

        })

    }

    override fun getAllUser(callback: (Boolean, List<UserModel>) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var allUsers = mutableListOf<UserModel>()
                    for(data in snapshot.children){
                        val user = data.getValue(UserModel::class.java)
                        if(user !=null){
                            allUsers.add(user)
                        }
                    }
                    callback(true,allUsers)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false,emptyList())
            }

        })
    }

    override fun getcurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun deleteUser(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"User Deleted Sucessfulyy")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun updateUser(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(model.toMap()).addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"User Updated Sucessfulyy")
            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun logout(
        userId: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signOut()
    }
}