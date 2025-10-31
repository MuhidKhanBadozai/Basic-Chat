package com.example.basics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DesignPage() {
    // Column = vertical layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // outer spacing
        verticalArrangement = Arrangement.Center, // center vertically
        horizontalAlignment = Alignment.CenterHorizontally // center horizontally
    ) {

        // Top row with two side-by-side boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // equal space between boxes
        ) {
            // Left box
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray.copy(alpha = 0.4f)), // semi-transparent grey
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Task 1", color = Color.Black, fontSize = 16.sp)
            }

            // Right box
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.Gray.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Task 2", color = Color.Black, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // space between row and bottom box

        // Bottom big box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Bottom Task Box", color = Color.Black, fontSize = 18.sp)
        }
    }
}
