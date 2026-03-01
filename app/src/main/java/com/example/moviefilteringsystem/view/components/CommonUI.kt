package com.example.moviefilteringsystem.view.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun smartFlixTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFFFD700),
    unfocusedBorderColor = Color.DarkGray,
    focusedLabelColor = Color(0xFFFFD700),
    unfocusedLabelColor = Color.Gray,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFFFFD700),
    focusedLeadingIconColor = Color(0xFFFFD700),
    unfocusedLeadingIconColor = Color.Gray,
    focusedTrailingIconColor = Color(0xFFFFD700),
    unfocusedTrailingIconColor = Color.Gray
)
