package com.budgetquest.android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.budgetquest.android.ui.theme.BudgetQuestTheme
import com.budgetquest.android.navigation.MainAppShell

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val prefs = getSharedPreferences("budgetquest_prefs", Context.MODE_PRIVATE)
        
        setContent {
            var isOnboarded by remember { 
                mutableStateOf(prefs.getBoolean("is_onboarded", false)) 
            }
            var isLoggedIn by remember {
                mutableStateOf(prefs.getBoolean("is_logged_in", false))
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
