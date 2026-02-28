package com.example.moviefilteringsystem.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.viewmodel.UserViewModel

@Composable
fun ManageUsersScreen() {
    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    var users by remember { mutableStateOf<List<UserModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        userViewModel.getUsers { users = it ?: emptyList() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(users) {
            UserItem(user = it, onDelete = {
                userViewModel.deleteUser(it.id) { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

@Composable
fun UserItem(user: UserModel, onDelete: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = user.name)
            Text(text = user.email)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}
