package com.example.colegiomilitargo.screens

import android.app.DatePickerDialog
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.colegiomilitargo.R
import com.example.colegiomilitargo.remote.FirebaseAuthRepository
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun CadastrarScreen(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var nomeError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var senhaError by remember { mutableStateOf("") }
    var dataError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { FirebaseAuthRepository() }

    // Cores do tema militar
    val militaryGreen = Color(0xFF4A5D23)
    val militaryGold = Color(0xFFD4AF37)
    val lightGray = Color(0xFFF5F5F5)
    val darkGreen = Color(0xFF0D5C3D)

    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                dataNascimento = String.format("%02d/%02d/%04d", day, month + 1, year)
                dataError = ""
            },
            calendar.get(Calendar.YEAR) - 18,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    fun validarCampos(): Boolean {
        var isValid = true

        if (nome.isBlank()) {
            nomeError = "Nome é obrigatório"
            isValid = false
        } else if (nome.length < 3) {
            nomeError = "Nome deve ter pelo menos 3 caracteres"
            isValid = false
        } else {
            nomeError = ""
        }

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

        if (dataNascimento.isBlank()) {
            dataError = "Data de nascimento é obrigatória"
            isValid = false
        } else {
            dataError = ""
        }

        return isValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header com botão voltar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "CADASTRO UNIFICADO",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp),
                        letterSpacing = 1.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Logo com fundo verde
                Card(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = darkGreen
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pmlogo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(160.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Título "CADASTRO COLÉGIO MILITAR"
                Text(
                    text = "CADASTRO COLÉGIO MILITAR",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Linha dourada decorativa
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(3.dp)
                        .background(militaryGold)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo Nome Completo
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "NOME COMPLETO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = nome,
                        onValueChange = {
                            nome = it
                            if (nomeError.isNotEmpty()) nomeError = ""
                        },
                        placeholder = { Text("Nome Completo do Aluno", color = Color.LightGray, fontSize = 14.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = militaryGreen,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        isError = nomeError.isNotEmpty(),
                        enabled = !isLoading
                    )

                    if (nomeError.isNotEmpty()) {
                        Text(
                            text = nomeError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Email
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "EMAIL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it.lowercase().trim()
                            if (emailError.isNotEmpty()) emailError = ""
                        },
                        placeholder = { Text("email@institucional.com.br", color = Color.LightGray, fontSize = 14.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(4.dp),
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
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Senha
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "SENHA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = senha,
                        onValueChange = {
                            senha = it
                            if (senhaError.isNotEmpty()) senhaError = ""
                        },
                        placeholder = { Text("••••••••", color = Color.LightGray) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(4.dp),
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
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Data de Nascimento
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "DATA DE NASCIMENTO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = dataNascimento,
                        onValueChange = { },
                        placeholder = { Text("mm/dd/yyyy", color = Color.LightGray, fontSize = 14.sp) },
                        singleLine = true,
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { showDatePicker() },
                        shape = RoundedCornerShape(4.dp),
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Selecionar Data",
                                    tint = Color.Gray
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.LightGray,
                            disabledPlaceholderColor = Color.LightGray,
                            disabledContainerColor = Color.White
                        ),
                        isError = dataError.isNotEmpty(),
                        enabled = false
                    )

                    if (dataError.isNotEmpty()) {
                        Text(
                            text = dataError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Mensagem de erro geral
                if (errorMessage.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botão FINALIZAR CADASTRO
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = militaryGreen,
                        contentColor = Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "FINALIZAR CADASTRO",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Texto regulamento
                Text(
                    text = "AO PROSSEGUIR, VOCÊ DECLARA ESTAR CIENTE DO",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "REGULAMENTO DISCIPLINAR INTERNO",
                    fontSize = 11.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "E DAS NORMAS DE CONDUTA ACADÊMICA.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Linha decorativa tripla
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .background(militaryGreen)
                    )
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .background(militaryGold)
                    )
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(3.dp)
                            .background(militaryGreen)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rodapé
                Text(
                    text = "SISTEMA UNIFICADO DE GESTÃO ESCOLAR",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}