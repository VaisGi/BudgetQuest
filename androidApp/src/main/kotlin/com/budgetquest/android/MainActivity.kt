package com.budgetquest.android

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.budgetquest.android.ui.theme.BudgetQuestTheme
import com.budgetquest.android.navigation.MainAppShell

import com.budgetquest.android.security.BiometricPromptManager
import com.budgetquest.android.security.TokenVault

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val prefs = getSharedPreferences("budgetquest_prefs", Context.MODE_PRIVATE)
        val tokenVault = TokenVault(this)

        val biometricPromptManager by lazy {
            BiometricPromptManager(this)
        }
        
        setContent {
            var isOnboarded by remember { 
                mutableStateOf(prefs.getBoolean("is_onboarded", false)) 
            }
            var isLoggedIn by remember {
                mutableStateOf(tokenVault.getToken(TokenVault.KEY_ACCESS_TOKEN) != null)
            }

            BudgetQuestTheme {
                MainAppShell(
                    isOnboarded = isOnboarded,
                    isLoggedIn = isLoggedIn,
                    onCompleteOnboarding = {
                        prefs.edit().putBoolean("is_onboarded", true).apply()
                        isOnboarded = true
                    }
                )
            }
        }
    }
}
