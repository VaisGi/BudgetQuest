package com.budgetquest.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.budgetquest.android.ui.components.BQGlassCard
import com.budgetquest.android.ui.theme.BQTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    emailOrPhone: String,
    onLoginSuccess: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("") }
    
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        BQGlassCard(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Verify OTP",
                    style = BQTypography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "A 6-digit code has been sent to $emailOrPhone",
                    style = BQTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { 
                        if (it.length <= 6) {
                            otpCode = it 
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Enter 6-digit code") },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, letterSpacing = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { 
                        if (otpCode.length == 6) {
                            // Hit backend, on success skip to Dashboard
                            onLoginSuccess()
                        }
                    },
                    enabled = otpCode.length == 6,
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Verify & Secure Login")
                }
                
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}
