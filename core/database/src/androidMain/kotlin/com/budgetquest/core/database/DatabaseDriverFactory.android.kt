package com.budgetquest.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.budgetquest.db.BudgetQuestDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(BudgetQuestDatabase.Schema, context, "BudgetQuest.db")
    }
}
