package com.example.moviefilteringsystem.viewmodel

import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.*

class UserViewModelTest {

    @Test
    fun login_success_test() {
        // Arrange
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)
        val testEmail = "test@gmail.com"
        val testPassword = "password123"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq(testEmail), eq(testPassword), any())

        var successResult = false
        var messageResult: String? = ""

        // Act
        viewModel.login(testEmail, testPassword) { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert
        assertTrue(successResult)
        assertEquals("Login success", messageResult)
        verify(repo).login(eq(testEmail), eq(testPassword), any())
    }

    @Test
    fun createUser_success_test() {
        // Arrange
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)
        val testUser = UserModel(name = "Test User", email = "test@gmail.com", password = "password123")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(1)
            callback(true, "User created successfully")
            null
        }.`when`(repo).createUser(any(), any())

        var successResult = false
        var messageResult: String? = ""

        // Act
        viewModel.createUser(testUser) { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Assert
        assertTrue(successResult)
        assertEquals("User created successfully", messageResult)
        verify(repo).createUser(eq(testUser), any())
    }
}
