package com.example.colegiomilitargo.screens

import android.app.DatePickerDialog
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.colegiomilitargo.R
import com.example.colegiomilitargo.remote.FirebaseAuthRepository
import com.example.colegiomilitargo.components.FormScreen




import kotlinx.coroutines.launch
import java.util.*

@Composable
fun CadastrarScreen(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // ✅ Validações em tempo real
    var nomeError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var senhaError by remember { mutableStateOf("") }
    var confirmarSenhaError by remember { mutableStateOf("") }
    var dataError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            dataNascimento = String.format("%02d/%02d/%04d", day, month + 1, year)
            dataError = ""
        },
        calendar.get(Calendar.YEAR) - 18, // Começa 18 anos atrás
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        // Define data máxima (hoje) para evitar datas futuras
        datePicker.maxDate = System.currentTimeMillis()
    }

    // ✅ Função de validação
    fun validarCampos(): Boolean {
        var isValid = true

        // Validar nome
        if (nome.isBlank()) {
            nomeError = "Nome é obrigatório"
            isValid = false
        } else if (nome.length < 3) {
            nomeError = "Nome deve ter pelo menos 3 caracteres"
            isValid = false
        } else {
            nomeError = ""
        }

        // Validar email
        if (email.isBlank()) {
            emailError = "Email é obrigatório"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Email inválido"
            isValid = false
        } else {
            emailError = ""
        }

        // Validar senha
        if (senha.isBlank()) {
            senhaError = "Senha é obrigatória"
            isValid = false
        } else if (senha.length < 6) {
            senhaError = "Senha deve ter pelo menos 6 caracteres"
            isValid = false
        } else {
            senhaError = ""
        }

        // Validar confirmação de senha
        if (confirmarSenha.isBlank()) {
            confirmarSenhaError = "Confirme a senha"
            isValid = false
        } else if (senha != confirmarSenha) {
            confirmarSenhaError = "As senhas não coincidem"
            isValid = false
        } else {
            confirmarSenhaError = ""
        }

        // Validar data
        if (dataNascimento.isBlank()) {
            dataError = "Data de nascimento é obrigatória"
            isValid = false
        } else {
            dataError = ""
        }

        return isValid
    }

    FormScreen(title = "Cadastro", logoRes = R.drawable.pmlogo) {

        // Campo Nome
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                if (nomeError.isNotEmpty()) nomeError = ""
            },
            label = { Text("Nome Completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = nomeError.isNotEmpty(),
            supportingText = {
                if (nomeError.isNotEmpty()) {
                    Text(text = nomeError, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Email
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
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Senha
        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                if (senhaError.isNotEmpty()) senhaError = ""
            },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = senhaError.isNotEmpty(),
            supportingText = {
                if (senhaError.isNotEmpty()) {
                    Text(text = senhaError, color = MaterialTheme.colorScheme.error)
                } else {
                    Text(text = "Mínimo 6 caracteres", color = Color.Gray)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Confirmar Senha
        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = {
                confirmarSenha = it
                if (confirmarSenhaError.isNotEmpty()) confirmarSenhaError = ""
            },
            label = { Text("Confirmar Senha") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmarSenhaError.isNotEmpty(),
            supportingText = {
                if (confirmarSenhaError.isNotEmpty()) {
                    Text(text = confirmarSenhaError, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Data de Nascimento
        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { },
            label = { Text("Data de Nascimento") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar Data"
                    )
                }
            },
            isError = dataError.isNotEmpty(),
            supportingText = {
                if (dataError.isNotEmpty()) {
                    Text(text = dataError, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mensagem de erro geral
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

        // Botão Cadastrar
        Button(
            onClick = {
                if (!validarCampos()) return@Button

                scope.launch {
                    isLoading = true
                    errorMessage = ""

                    val result = repository.cadastrarUsuario(
                        nome = nome,
                        email = email,
                        senha = senha,
                        dataNascimento = dataNascimento
                    )

                    result.onSuccess {
                        // ✅ Sucesso! Navegar para home
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }.onFailure { error ->
                        errorMessage = when {
                            error.message?.contains("email address is already in use") == true ->
                                "Este email já está cadastrado"
                            error.message?.contains("network error") == true ->
                                "Erro de conexão. Verifique sua internet."
                            else -> error.message ?: "Erro ao cadastrar"
                        }
                    }

                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "Cadastrando..." else "Cadastrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Voltar
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Já tem conta? Faça login")
        }
    }
}
