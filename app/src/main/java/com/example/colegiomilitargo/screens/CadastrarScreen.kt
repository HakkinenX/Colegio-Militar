package com.example.logincompose.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.seuprojeto.colegioMilitarGO.R
import com.example.logincompose.components.FormScreen
import java.util.*

@Composable
fun CadastrarScreen(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") } // campo de data

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            dataNascimento = "${day}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    FormScreen(title = "Cadastro", logoRes = R.drawable.pmlogo) {
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome Completo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TextField para Data de Nascimento clicável
        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { /* não permite digitar manualmente */ },
            label = { Text("Data de Nascimento") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selecionar Data"
                    )
                }
            },
            // Quando o usuário clicar no campo, também abrirá o DatePicker
            interactionSource = remember { MutableInteractionSource() }.also { source ->
                LaunchedEffect(source) {
                    source.interactions.collect { interaction ->
                        datePickerDialog.show()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* salvar cadastro */ },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cadastrar") }
    }
}
