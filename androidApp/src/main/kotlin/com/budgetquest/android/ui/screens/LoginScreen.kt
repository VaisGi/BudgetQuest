package com.budgetquest.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.budgetquest.android.ui.components.BQGlassCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToOtp: (String) -> Unit,
    onNavigateToForgotPassword: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isEmailMode by remember { mutableStateOf(true) }
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        BQGlassCard(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Segmented Toggle
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Button(
                        onClick = { isEmailMode = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEmailMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isEmailMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Email")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isEmailMode = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isEmailMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (!isEmailMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Mobile")
                    }
                }

                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text(if (isEmailMode) "Email Address" else "Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isEmailMode) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 8.dp)
                            .clickable { onNavigateToForgotPassword() }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { 
                        if (isEmailMode) {
                            // Hit backend, on success skip to Dashboard
                        } else {
                            onNavigateToOtp(identifier) 
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(if (isEmailMode) "Log In Securely" else "Send OTP Code")
                }
            }
        }
    }
}
