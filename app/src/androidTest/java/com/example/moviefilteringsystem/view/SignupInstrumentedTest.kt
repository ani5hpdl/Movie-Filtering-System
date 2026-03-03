package com.example.moviefilteringsystem.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviefilteringsystem.view.SignupActivity
import com.example.moviefilteringsystem.view.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignupInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignupActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testSignupFlow() {
        // Fill in full name
        composeTestRule.onNodeWithTag("fullName")
            .performTextInput("Test User")

        // Fill in email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("newuser@email.com")

        // Fill in contact number
        composeTestRule.onNodeWithTag("contactNumber")
            .performTextInput("1234567890")

        // Fill in password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("password123")

        // Click Sign Up
        composeTestRule.onNodeWithTag("signupButton")
            .performClick()
    }

    @Test
    fun testNavigateToLogin() {
        // Click on "Sign In" link
        composeTestRule.onNodeWithTag("loginLink")
            .performClick()

        // Verify that LoginActivity is launched
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }
}
