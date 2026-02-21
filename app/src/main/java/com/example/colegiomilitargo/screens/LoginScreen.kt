package com.example.colegiomilitargo.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colegiomilitargo.R
import com.example.colegiomilitargo.remote.FirebaseAuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {

    val repository = remember { FirebaseAuthRepository() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val militaryGreen = Color(0xFF4A5D23)
    val militaryGold = Color(0xFFD4AF37)
    val lightGray = Color(0xFFF5F5F5)
    val darkGreen = Color(0xFF0D5C3D)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(8.dp))

            Text(
                text = "COLÉGIO MILITAR DE GOIÁS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.size(140.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = darkGreen),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pmlogo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(120.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(2.dp)
                    .background(militaryGold)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "ACESSO AO\nSISTEMA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = militaryGreen,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Portal Unificado do Aluno e Servidor",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, null, tint = militaryGreen, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("EMAIL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = militaryGreen)
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    placeholder = { Text("seu.email@exemplo.com", color = Color.LightGray) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = militaryGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    enabled = !loading
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, null, tint = militaryGreen, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("SENHA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = militaryGreen)
                }

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    placeholder = { Text("••••••••", color = Color.LightGray) },
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Text(if (senhaVisivel) "👁" else "👁‍🗨", fontSize = 18.sp)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = militaryGreen,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    enabled = !loading
                )
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = { /* TODO: Implementar recuperação de senha */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Esqueceu a senha?",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    if (
                        email.isBlank() ||
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ||
                        senha.length < 6
                    ) {
                        error = "Email ou senha inválidos"
                        return@Button
                    }

                    scope.launch {
                        loading = true
                        error = ""

                        val result = repository.fazerLogin(email, senha)

                        result.onSuccess {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }.onFailure {
                            error = "Email ou senha inválidos"
                        }

                        loading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !loading,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = militaryGreen,
                    contentColor = Color.White
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "ENTRAR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.navigate("cadastrar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !loading,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = militaryGreen),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp)
            ) {
                Text(
                    text = "PRIMEIRO ACESSO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(militaryGreen)
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(militaryGold)
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(militaryGreen)
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "SECRETARIA DE SEGURANÇA PÚBLICA",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    letterSpacing = 0.4.sp
                )
                Text(
                    text = "POLÍCIA MILITAR DO ESTADO DE GOIÁS",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    letterSpacing = 0.4.sp
                )
            }
        }
    }
}