package com.example.listadecompras

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.safeDrawingPadding

data class ShoppingItem(
    val id: Int,
    val name: String
)

@Composable
fun ShoppingListItem(item: ShoppingItem, onRemove: (ShoppingItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(2.dp), // Sombra
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nome do item
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )

            // Botão de Remover
            IconButton(
                onClick = { onRemove(item) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remover ${item.name}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ShoppingListScreen() {
    val shoppingList = remember { mutableStateListOf<ShoppingItem>() }

    var itemNameInput by remember { mutableStateOf("") }
    var nextItemId by remember { mutableStateOf(1) }

    Column (
        modifier = Modifier
            .safeDrawingPadding() // Padding para evitar áreas seguras
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lista de Compras",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = itemNameInput,
                onValueChange = { itemNameInput = it },
                label = {Text(
                        "Nome do item",
                        style = MaterialTheme.typography.bodyMedium
                )},
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            )

            Button(
                onClick = {
                    val trimmedName = itemNameInput.trim() // trim = aparar
                    if (trimmedName.isNotEmpty()) {
                        shoppingList.add(
                            ShoppingItem(
                                id = nextItemId++,
                                name = trimmedName
                            )
                        )
                        itemNameInput = ""
                    }
                },
                enabled = itemNameInput.trim().isNotEmpty() // Habilita apenas se não estiver vazio
            ) {
                Text("Adicionar")
            }
        }

        // CONTADOR DE ITENS
        Text(
            text = "Total de Itens: ${shoppingList.size}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // LISTA
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(shoppingList, key = { it.id }) { item ->
                ShoppingListItem(
                    item = item,
                    onRemove = { itemToRemove ->
                        shoppingList.remove(itemToRemove)
                    }
                )
            }

            // Lista Vazia
            if (shoppingList.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sua lista está vazia! Adicione um item.")
                    }
                }
            }
        }
    }
}