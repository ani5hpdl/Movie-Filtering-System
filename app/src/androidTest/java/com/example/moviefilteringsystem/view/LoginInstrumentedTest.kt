package com.example.moviefilteringsystem.view

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviefilteringsystem.view.LoginActivity
import com.example.moviefilteringsystem.view.DashboardActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testLoginFlow() {
        // Fill in email
        composeTestRule.onNodeWithTag("email")
            .performTextInput("test@email.com")

        // Fill in password
        composeTestRule.onNodeWithTag("password")
            .performTextInput("123456")

        // Click Login
        composeTestRule.onNodeWithTag("loginButton")
            .performClick()

        // Note: Actual Firebase login takes time and depends on network.
        // In a real instrumental test, you might want to mock the ViewModel 
        // or check for an expected UI change after login.
    }

    @Test
    fun testNavigateToSignup() {
        // Click on "Create Account" link
        composeTestRule.onNodeWithTag("signupLink")
            .performClick()

        // Verify that SignupActivity is launched
        Intents.intended(hasComponent(SignupActivity::class.java.name))
    }
}
