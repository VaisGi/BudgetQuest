package com.budgetquest.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.budgetquest.android.ui.theme.BudgetQuestTheme
import com.budgetquest.android.navigation.BudgetQuestNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetQuestTheme {
                BudgetQuestNavHost()
            }
        }
    }
}
