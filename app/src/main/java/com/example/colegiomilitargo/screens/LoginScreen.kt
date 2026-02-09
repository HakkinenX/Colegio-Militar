package com.example.colegiomilitargo.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var senhaError by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (repository.isUserLoggedIn()) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    fun validarCampos(): Boolean {
        var isValid = true

        if (email.isBlank()) {
            emailError = "Email é obrigatório"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Email inválido"
            isValid = false
        } else {
            emailError = ""
        }

        if (senha.isBlank()) {
            senhaError = "Senha é obrigatória"
            isValid = false
        } else if (senha.length < 6) {
            senhaError = "Senha deve ter pelo menos 6 caracteres"
            isValid = false
        } else {
            senhaError = ""
        }

        return isValid
    }

    // Cores do tema militar
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
            Spacer(modifier = Modifier.height(8.dp))

            // Título superior
            Text(
                text = "COLÉGIO MILITAR DE GOIÁS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Logo com fundo verde - AINDA MENOR
            Card(
                modifier = Modifier.size(140.dp), // ✅ Reduzido de 180dp para 140dp
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = darkGreen
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pmlogo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(120.dp) // ✅ Reduzido de 160dp para 120dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Linha decorativa dourada
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(2.dp)
                    .background(militaryGold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título "ACESSO AO SISTEMA"
            Text(
                text = "ACESSO AO\nSISTEMA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = militaryGreen,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo
            Text(
                text = "Portal Unificado do Aluno e Servidor",
                fontSize = 13.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Email
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = militaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EMAIL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = militaryGreen,
                        letterSpacing = 0.5.sp
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it.lowercase().trim()
                        if (emailError.isNotEmpty()) emailError = ""
                    },
                    placeholder = { Text("seu.email@exemplo.com", color = Color.LightGray, fontSize = 14.sp) },
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
                    isError = emailError.isNotEmpty(),
                    enabled = !isLoading
                )

                if (emailError.isNotEmpty()) {
                    Text(
                        text = emailError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = militaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SENHA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = militaryGreen,
                        letterSpacing = 0.5.sp
                    )
                }

                OutlinedTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        if (senhaError.isNotEmpty()) senhaError = ""
                    },
                    placeholder = { Text("••••••••", color = Color.LightGray) },
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = { senhaVisivel = !senhaVisivel },
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(
                                text = if (senhaVisivel) "👁" else "👁‍🗨",
                                fontSize = 18.sp,
                                color = Color.Gray
                            )
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
                    isError = senhaError.isNotEmpty(),
                    enabled = !isLoading
                )

                if (senhaError.isNotEmpty()) {
                    Text(
                        text = senhaError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Link "Esqueceu a senha?"
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

            Spacer(modifier = Modifier.height(12.dp))

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botão ENTRAR
            Button(
                onClick = {
                    if (!validarCampos()) return@Button

                    scope.launch {
                        isLoading = true
                        errorMessage = ""

                        val result = repository.fazerLogin(
                            email = email,
                            senha = senha
                        )

                        result.onSuccess {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }.onFailure { error ->
                            errorMessage = when {
                                error.message?.contains("no user record") == true ||
                                        error.message?.contains("invalid-credential") == true ||
                                        error.message?.contains("wrong-password") == true ->
                                    "Email ou senha incorretos"
                                error.message?.contains("network error") == true ->
                                    "Erro de conexão. Verifique sua internet."
                                error.message?.contains("too-many-requests") == true ->
                                    "Muitas tentativas. Tente novamente mais tarde."
                                else -> "Erro ao fazer login: ${error.message}"
                            }
                        }

                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = militaryGreen,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botão PRIMEIRO ACESSO
            OutlinedButton(
                onClick = { navController.navigate("cadastrar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = militaryGreen
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp
                )
            ) {
                Text(
                    text = "PRIMEIRO ACESSO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Linha decorativa tripla
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

            Spacer(modifier = Modifier.height(12.dp))

            // Rodapé
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