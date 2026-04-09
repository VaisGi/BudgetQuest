package com.budgetquest.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.budgetquest.db.BudgetQuestDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(BudgetQuestDatabase.Schema, "BudgetQuest.db")
    }
}
