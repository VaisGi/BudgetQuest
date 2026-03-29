---
name: "SQLDelight Offline-First"
description: "Best practices for using SQLDelight for local data persistence in KMP apps."
---

# SQLDelight Offline-First

BudgetQuest relies on **SQLDelight** to generate type-safe Kotlin APIs from SQL statements. The app is currently offline-first, meaning all data operations happen against the local database before syncing (when backend is added).

## Core Principles
1. **Schema First**: Define all tables and queries in `.sq` files located in `shared/src/commonMain/sqldelight/com/budgetquest/db/`.
2. **Boolean Trap**: Due to a bug in SQLDelight 2.x, using `INTEGER AS Boolean` generates invalid imports without specific setup. **Use `INTEGER` directly** for boolean fields (0 = false, 1 = true) to completely avoid codegen issues.
3. **Reactive Repositories**: Use SQLDelight's coroutine extensions (`asFlow()`, `mapToList()`) to expose reactive streams from DAOs to the UseCase layer.

## Repository Pattern
```kotlin
class LocalTransactionRepository(
    private val db: BudgetQuestDatabase
) : TransactionRepository {
    
    override fun getTransactions(): Flow<List<Transaction>> {
        return db.budgetQuestDatabaseQueries
            .getAllTransactions()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toDomain() } }
    }
}
```

## Anti-Patterns
- **🚫 NO SQLite logic in UseCases**: SQL queries reside exclusively in `.sq` files. Repositories bridge DAOs to domain models.
- **🚫 Avoid `Thread.sleep` or blocking calls**: Always use `Dispatchers.IO` (or default if unconfined) when calling database operations.
