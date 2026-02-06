package com.example.colegiomilitargo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.colegiomilitargo.components.FormScreen
import com.example.colegiomilitargo.R

@Composable
fun HomeScreen(navController: NavController) {

    FormScreen(
        logoRes = R.drawable.pmlogo,
        logoSize = 200.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ImageButtonWithText(
                    imageRes = R.drawable.geral,
                    text = "Geral",
                    onClick = { navController.navigate("geral") },
                    modifier = Modifier.weight(1f)
                )
                ImageButtonWithText(
                    imageRes = R.drawable.formulario,
                    text = "Formulário",
                    onClick = { navController.navigate("formulario") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ImageButtonWithText(
                    imageRes = R.drawable.estudos,
                    text = "Estudos",
                    onClick = { navController.navigate("estudos") },
                    modifier = Modifier.weight(1f)
                )
                ImageButtonWithText(
                    imageRes = R.drawable.cameras,
                    text = "Câmeras",
                    onClick = { navController.navigate("cameras") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ImageButtonWithText(
    imageRes: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
