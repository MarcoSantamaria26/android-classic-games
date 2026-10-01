package com.example.classicgames

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MenuScreen(modifier: Modifier = Modifier) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf("tateti") }
    var mode by remember { mutableStateOf(0) } // 0 = Vs. Máquina, 1 = Vs. Otro jugador
    var difficulty by remember { mutableStateOf(0) } // 0 = Fácil, 1 = Medio, 2 = Difícil
    var games by remember { mutableStateOf("3") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .imePadding() // <--- ESTO EMPUJA EL CONTENIDO CUANDO SALE EL TECLADO
            .navigationBarsPadding() // <--- Evita que tape la barra de navegación del celu
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Classic Games",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Nombre del Jugador 1
        OutlinedTextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Tu nombre (Jugador 1)") },
            placeholder = { Text("Ej: Juan") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                coroutineScope.launch { scrollState.animateScrollTo(0) }
            }),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // --- SELECCIÓN DE JUEGO (3 Cuadrados) ---
        Text("Elegí el juego", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            GameCard(
                title = "Ta-Te-Ti",
                isSelected = selectedGame == "tateti",
                isEnabled = true,
                modifier = Modifier.weight(1f)
            ) { selectedGame = "tateti" }

            GameCard(
                title = "Ahorcado",
                isSelected = selectedGame == "ahorcado",
                isEnabled = false,
                modifier = Modifier.weight(1f)
            ) { }

            GameCard(
                title = "Batalla Naval",
                isSelected = selectedGame == "batalla",
                isEnabled = false,
                modifier = Modifier.weight(1f)
            ) { }
        }

        // Modo de juego
        Text("Modo de juego", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { mode = 0 }
            ) {
                RadioButton(selected = mode == 0, onClick = { mode = 0 })
                Text(text = "Vs. Máquina", modifier = Modifier.padding(start = 2.dp))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { mode = 1 }
            ) {
                RadioButton(selected = mode == 1, onClick = { mode = 1 })
                Text(text = "Vs. Otro", modifier = Modifier.padding(start = 2.dp))
            }
        }

        // Dificultad (si es Vs. Máquina) o Nombre del Jugador 2 (si es Vs. Otro)
        if (mode == 0) {
            Text("Nivel de dificultad", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                listOf("Fácil" to 0, "Medio" to 1, "Difícil" to 2).forEach { (label, diff) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { difficulty = diff }
                    ) {
                        RadioButton(selected = difficulty == diff, onClick = { difficulty = diff })
                        Text(text = label, modifier = Modifier.padding(start = 2.dp))
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = player2Name,
                onValueChange = { player2Name = it },
                label = { Text("Nombre del Jugador 2") },
                placeholder = { Text("Ej: María") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    coroutineScope.launch { scrollState.animateScrollTo(0) }
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(150)
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        }
                    }
            )
        }

        // Mejor de X juegos (con auto-scroll inteligente)
        OutlinedTextField(
            value = games,
            onValueChange = { games = it },
            label = { Text("Mejor de (partidas)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    // Vuelve la pantalla arriba al aceptar
                    coroutineScope.launch { scrollState.animateScrollTo(0) }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(150)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }
        )

        // Botón comenzar
        Button(
            onClick = {
                // TODO: Pasar a la pantalla del Ta-Te-Ti enviando estos datos
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 36.dp)
        ) {
            Text("Comenzar Partida")
        }
        // <--- ESPACIO EXTRA PARA QUE EL TECLADO NUNCA TAPE NADA AL BAJAR
        Spacer(modifier = Modifier.height(250.dp))
    }
}

@Composable
fun GameCard(
    title: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (isEnabled) {
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            if (!isEnabled) {
                Text(
                    text = "Próximamente",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}