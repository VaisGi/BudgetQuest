package com.budgetquest.core.database

import app.cash.sqldelight.ColumnAdapter
import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.BudgetStatus
import com.budgetquest.domain.model.QuestType
import com.budgetquest.domain.model.TransactionCategory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// ─── Enum Adapters ───────────────────────────────────────────────────────────

val transactionCategoryAdapter = object : ColumnAdapter<TransactionCategory, String> {
    override fun decode(databaseValue: String): TransactionCategory =
        TransactionCategory.valueOf(databaseValue)
    override fun encode(value: TransactionCategory): String = value.name
}

val questTypeAdapter = object : ColumnAdapter<QuestType, String> {
    override fun decode(databaseValue: String): QuestType = QuestType.valueOf(databaseValue)
    override fun encode(value: QuestType): String = value.name
}

val budgetStatusAdapter = object : ColumnAdapter<BudgetStatus, String> {
    override fun decode(databaseValue: String): BudgetStatus = BudgetStatus.valueOf(databaseValue)
    override fun encode(value: BudgetStatus): String = value.name
}

// ─── JSON Adapter ─────────────────────────────────────────────────────────────

val badgeListAdapter = object : ColumnAdapter<List<Badge>, String> {
    override fun decode(databaseValue: String): List<Badge> = try {
        Json.decodeFromString(databaseValue)
    } catch (e: Exception) {
        emptyList()
    }
    override fun encode(value: List<Badge>): String = Json.encodeToString(value)
}

// ─── Primitive Adapters ───────────────────────────────────────────────────────
// SQLite stores integers as Long — these map Kotlin Int ↔ Long

val intColumnAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int = databaseValue.toInt()
    override fun encode(value: Int): Long = value.toLong()
}
