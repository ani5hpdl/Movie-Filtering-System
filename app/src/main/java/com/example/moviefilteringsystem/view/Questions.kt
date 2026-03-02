package com.example.moviefilteringsystem.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviefilteringsystem.model.UserModel
import com.example.moviefilteringsystem.repository.UserRepoImpl
import com.example.moviefilteringsystem.view.ui.theme.MovieFilteringSystemTheme
import com.example.moviefilteringsystem.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun QuestionsScreen(onComplete: () -> Unit) {
    var showProfileQuestionnaire by remember { mutableStateOf(false) }
    var selectedGenres by remember { mutableStateOf(setOf<String>()) }

    if (showProfileQuestionnaire) {
        ProfileQuestionnaireScreen(
            selectedGenres = selectedGenres.toList(),
            onComplete = onComplete
        )
    } else {
        GenreSelectionScreen(
            onGenresSelected = { genres ->
                selectedGenres = genres
                showProfileQuestionnaire = true
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreSelectionScreen(onGenresSelected: (Set<String>) -> Unit) {
    val genres = listOf(
        "Action", "Comedy", "Romance", "Horror", "Mystery", "Thriller",
        "Sci-Fi", "Superhero", "Drama", "Musical", "Fantasy", "Crime",
        "Adventure", "Historic", "Sports", "Kids", "Family", "Anime", "Documentary"
    )

    var selectedGenres by remember { mutableStateOf(setOf<String>()) }
    val isButtonEnabled = selectedGenres.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFF1A1A1A))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PICK YOUR FAVOURITE GENRE",
            color = Color(0xFFFFD700), // Gold color
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
        )

        Text(
            text = "Select one or more genres to get personalized movie recommendations.",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 3
        ) {
            genres.forEach { genre ->
                val isSelected = selectedGenres.contains(genre)
                GenreChip(
                    genre = genre,
                    isSelected = isSelected,
                    onGenreClick = {
                        selectedGenres = if (isSelected) {
                            selectedGenres - genre
                        } else {
                            selectedGenres + genre
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onGenresSelected(selectedGenres) },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFD700),
                disabledContainerColor = Color.DarkGray
            )
        ) {
            Text(
                text = "Continue",
                color = if (isButtonEnabled) Color.Black else Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileQuestionnaireScreen(selectedGenres: List<String>, onComplete: () -> Unit) {
    var q1Selection by remember { mutableStateOf<String?>(null) }
    var q2Selection by remember { mutableStateOf<String?>(null) }
    var q3Selection by remember { mutableStateOf<String?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }
    val auth = FirebaseAuth.getInstance()

    val questions = listOf(
        "1. How do you experience movies" to listOf("I enjoy watching and sharing opinions!", "I analyze and review movies in depth!"),
        "2. What best describes your movie watching style?" to listOf("I watch for entertainment & share casual reviews", "I analyze storytelling, direction & performances"),
        "3. After watching a movie, what do you do next?" to listOf("I discuss it with friends & rate it casually", "I break down the plot, performances & themes")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CHOOSE YOUR PROFILE",
                color = Color(0xFFFFD700),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                QuestionItem(
                    question = questions[0].first,
                    options = questions[0].second,
                    selectedOption = q1Selection,
                    onOptionSelected = { q1Selection = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuestionItem(
                    question = questions[1].first,
                    options = questions[1].second,
                    selectedOption = q2Selection,
                    onOptionSelected = { q2Selection = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuestionItem(
                    question = questions[2].first,
                    options = questions[2].second,
                    selectedOption = q3Selection,
                    onOptionSelected = { q3Selection = it }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        if (isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFFD700))
            }
        } else {
            IconButton(
                onClick = {
                    if (q1Selection != null && q2Selection != null && q3Selection != null) {
                        isSaving = true
                        val userId = auth.currentUser?.uid
                        if (userId != null) {
                            userViewModel.getUser(userId) { user ->
                                if (user != null) {
                                    val updatedUser = user.copy(
                                        favoriteGenres = selectedGenres,
                                        questionnaireResults = mapOf(
                                            "q1" to q1Selection!!,
                                            "q2" to q2Selection!!,
                                            "q3" to q3Selection!!
                                        ),
                                        isQuestionnaireCompleted = true
                                    )
                                    userViewModel.updateUser(updatedUser) { success, message ->
                                        isSaving = false
                                        if (success) {
                                            onComplete()
                                        } else {
                                            Toast.makeText(context, message ?: "Failed to save", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    isSaving = false
                                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please answer all questions", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(Color(0xFFFFD700), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Finish",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column {
        Text(
            text = question,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = (selectedOption == option),
                    onCheckedChange = { onOptionSelected(option) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFFD700),
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = option, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun GenreChip(
    genre: String,
    isSelected: Boolean,
    onGenreClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFFD700) else Color.Transparent,
        label = "background color animation"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.White,
        label = "text color animation"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFFFD700) else Color.Gray,
        label = "border color animation"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .animateContentSize()
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(50)
            )
            .clickable(onClick = onGenreClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
