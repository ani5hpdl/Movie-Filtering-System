package com.example.moviefilteringsystem.viewmodel

import com.example.moviefilteringsystem.model.MovieModel
import com.example.moviefilteringsystem.repository.MovieRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MovieViewModelTest {

    @Test
    fun addMovie_success_test() {
        val repo = mock<MovieRepo>()
        val viewModel = MovieViewModel(repo)

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(1)
            callback(true, "Movie added successfully")
            null
        }.`when`(repo).addMovie(any(), any())

        var successResult = false
        var messageResult = ""

        viewModel.addMovie("Inception", "A dream within a dream", "url", 9.0, "Sci-Fi") { success, msg ->
            successResult = success
            messageResult = msg ?: ""
        }

        assertTrue(successResult)
        assertEquals("Movie added successfully", messageResult)

        verify(repo).addMovie(any(), any())
    }

    @Test
    fun deleteMovie_success_test() {
        val repo = mock<MovieRepo>()
        val viewModel = MovieViewModel(repo)
        val movieId = "movie123"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String?) -> Unit>(1)
            callback(true, "Movie deleted successfully")
            null
        }.`when`(repo).deleteMovie(eq(movieId), any())

        var successResult = false
        var messageResult = ""

        viewModel.deleteMovie(movieId) { success, msg ->
            successResult = success
            messageResult = msg ?: ""
        }

        assertTrue(successResult)
        assertEquals("Movie deleted successfully", messageResult)

        verify(repo).deleteMovie(eq(movieId), any())
    }
}
