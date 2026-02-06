package com.example.colegiomilitargo.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.colegiomilitargo.R
import com.example.colegiomilitargo.components.FormScreen
import com.example.colegiomilitargo.remote.FirebaseAuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
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

    FormScreen(title = "Login", logoRes = R.drawable.pmlogo) {

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.lowercase().trim()
                if (emailError.isNotEmpty()) emailError = ""
            },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty(),
            supportingText = {
                if (emailError.isNotEmpty()) {
                    Text(text = emailError, color = MaterialTheme.colorScheme.error)
                }
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                if (senhaError.isNotEmpty()) senhaError = ""
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = senhaError.isNotEmpty(),
            supportingText = {
                if (senhaError.isNotEmpty()) {
                    Text(text = senhaError, color = MaterialTheme.colorScheme.error)
                }
            },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Entrar")
                }
            }

            OutlinedButton(
                onClick = { navController.navigate("cadastrar") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Cadastrar")
            }
        }
    }
}
