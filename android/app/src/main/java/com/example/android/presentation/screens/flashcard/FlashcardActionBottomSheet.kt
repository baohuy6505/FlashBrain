package com.example.android.presentation.screens.flashcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.domain.model.Flashcard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardActionBottomSheet(
    existingCard: Flashcard?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var front by remember { mutableStateOf(existingCard?.frontText ?: "") }
    var back by remember { mutableStateOf(existingCard?.backText ?: "") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp).padding(bottom = 32.dp)) {
            Text(if (existingCard == null) "New Card" else "Edit Card", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = front, onValueChange = { front = it }, label = { Text("Front") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = back, onValueChange = { back = it }, label = { Text("Back") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onSave(front, back) },
                modifier = Modifier.fillMaxWidth(),
                enabled = front.isNotBlank() && back.isNotBlank()
            ) { Text("Save Card") }
        }
    }
}