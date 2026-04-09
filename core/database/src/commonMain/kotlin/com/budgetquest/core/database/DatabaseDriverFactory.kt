package com.budgetquest.core.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * Creates the BudgetQuestDatabase with all column adapters.
 * This is a plain factory function — Koin wiring lives in SharedModule.
 */
fun createBudgetQuestDatabase(driverFactory: DatabaseDriverFactory): com.budgetquest.db.BudgetQuestDatabase {
    return com.budgetquest.db.BudgetQuestDatabase(
        driver = driverFactory.createDriver(),
        BudgetEntityAdapter = com.budgetquest.db.BudgetEntity.Adapter(
            categoryAdapter = transactionCategoryAdapter
        ),
        QuestEntityAdapter = com.budgetquest.db.QuestEntity.Adapter(
            xpRewardAdapter = intColumnAdapter,
            typeAdapter = questTypeAdapter
        ),
        TransactionEntityAdapter = com.budgetquest.db.TransactionEntity.Adapter(
            categoryAdapter = transactionCategoryAdapter
        ),
        UserProfileEntityAdapter = com.budgetquest.db.UserProfileEntity.Adapter(
            levelAdapter = intColumnAdapter,
            totalXpAdapter = intColumnAdapter,
            currentStreakAdapter = intColumnAdapter,
            longestStreakAdapter = intColumnAdapter,
            badgesAdapter = badgeListAdapter
        )
    )
}
