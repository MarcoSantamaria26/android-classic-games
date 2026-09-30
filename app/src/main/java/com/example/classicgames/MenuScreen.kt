package com.example.classicgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.radioButton
import androidx.compose.foundation.rememberRadioGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.intPx

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClassicGamesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MenuContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MenuContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Classic Games",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Nombre del jugador
        OutlinedTextField(
            value = "",
            onValueChange = { },
            label = { Text("Tu nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Modo de juego
        Text("Modo de juego", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RadioGroup(
                value = 1,
                onValueChange = { },
                enabled = false // Placeholder, habilitar cuando haya nombre
            ) {
                RadioButton(
                    value = 0,
                    text = "Vs. Máquina"
                )
                RadioButton(
                    value = 1,
                    text = "Vs. Otro jugador"
                )
            }
        }

        // Nivel de dificultad (solo vs máquina)
        // ... agregar later

        // Mejor de X juegos
        OutlinedTextField(
            value = "3",
            onValueChange = { },
            label = { Text("Mejor de") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Botón comenzar
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Text("Comenzar Partida")
        }
    }
}