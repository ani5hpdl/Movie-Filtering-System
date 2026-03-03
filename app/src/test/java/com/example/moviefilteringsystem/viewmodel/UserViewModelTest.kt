package com.example.moviefilteringsystem.viewmodel

import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    @Test
    fun login_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg ?: ""
        }

        assertTrue(successResult)
        assertEquals("Login success", messageResult)

        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }

    @Test
    fun createUser_success_test() {
        val repo = mock<UserRepo>()
        val viewModel = UserViewModel(repo)
        val testUser = UserModel(name = "Test", email = "test@gmail.com", password = "password")

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(1)
            callback(true, "Signup success")
            null
        }.`when`(repo).createUser(any(), any())

        var successResult = false
        var messageResult = ""

        viewModel.createUser(testUser) { success, msg ->
            successResult = success
            messageResult = msg ?: ""
        }

        assertTrue(successResult)
        assertEquals("Signup success", messageResult)

        verify(repo).createUser(any(), any())
    }
}
