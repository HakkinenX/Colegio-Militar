package com.example.colegiomilitargo.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun TestFirebaseScreen() {

    var status by remember { mutableStateOf("Aguardando teste...") }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("teste")
            .document("ping")
            .set(mapOf("status" to "ok"))
            .addOnSuccessListener {
                status = "✅ Firestore conectado com sucesso"
            }
            .addOnFailureListener { e ->
                status = "❌ Erro: ${e.message}"
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = status)
    }
}
