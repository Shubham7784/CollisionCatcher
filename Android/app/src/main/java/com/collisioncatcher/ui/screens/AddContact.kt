package com.collisioncatcher.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.collisioncatcher.retrofit.entity.Member
import com.collisioncatcher.viewmodel.UserViewModel

@Composable
fun AddContact(onSuccess: ()->Unit,context: Context,viewModel: UserViewModel = viewModel()) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var relation by remember { mutableStateOf(TextFieldValue("")) }

    if(viewModel.isSuccess.value)
    {
        onSuccess()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Profile Initials Preview
        Box(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally)
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(40.dp)),
            contentAlignment = Alignment.Center
        ) {
            val initials = name.text.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").uppercase()
            Text(
                text = initials,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone Input
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Relation Input
        OutlinedTextField(
            value = relation,
            onValueChange = { relation = it },
            label = { Text("Relation") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                if (name.text.isNotBlank() && phone.text.isNotBlank() && relation.text.isNotBlank()) {
                    val member  = Member(null,name.text,phone.text,relation.text)
                    viewModel.saveMember(context,member)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if(viewModel.isLoading.value)
            {
                CircularProgressIndicator(color = Color.Blue, modifier = Modifier.size(14.dp))
            }
            else
            {
                Text("Save Contact", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
