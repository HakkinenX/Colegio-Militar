package com.example.logincompose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen(
    title: String = "",
    logoRes: Int? = null,
    logoSize: Dp = 200.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF31FC76),
                            Color(0xFF24C95B),
                            Color(0xFF069332),
                            Color(0xFF01601E),
                            Color(0xFF00310F)
                        )
                    )
                )
                .blur(12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            logoRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Logo",
                    modifier = Modifier.size(logoSize)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            content()
        }
    }
}
