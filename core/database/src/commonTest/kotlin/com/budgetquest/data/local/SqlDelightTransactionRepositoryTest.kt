package com.budgetquest.data.local

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.budgetquest.core.database.*
import com.budgetquest.db.BudgetQuestDatabase
import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.TransactionCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.*

class SqlDelightTransactionRepositoryTest {

    private lateinit var repo: SqlDelightTransactionRepository
    private lateinit var driver: JdbcSqliteDriver

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BudgetQuestDatabase.Schema.create(driver)
        val queries = BudgetQuestDatabase(
            driver = driver,
            BudgetEntityAdapter = com.budgetquest.db.BudgetEntity.Adapter(transactionCategoryAdapter),
            QuestEntityAdapter = com.budgetquest.db.QuestEntity.Adapter(intColumnAdapter, questTypeAdapter),
            TransactionEntityAdapter = com.budgetquest.db.TransactionEntity.Adapter(transactionCategoryAdapter),
            UserProfileEntityAdapter = com.budgetquest.db.UserProfileEntity.Adapter(
                intColumnAdapter, intColumnAdapter, intColumnAdapter, intColumnAdapter, badgeListAdapter
            )
        ).budgetQuestDatabaseQueries
        repo = SqlDelightTransactionRepository(queries)
    }

    @AfterTest
    fun teardown() = driver.close()

    @Test
    fun `addTransaction stores and retrieves by monthYear`() = runTest {
        val tx = Transaction(
            id = "tx1",
            amount = 50.0,
            category = TransactionCategory.FOOD,
            description = "Lunch",
            date = LocalDate(2026, 4, 9),
            isExpense = true
        )
        repo.addTransaction(tx)

        val results = repo.getTransactions("2026-04").first()
        assertEquals(1, results.size)
        assertEquals("tx1", results.first().id)
        assertEquals(50.0, results.first().amount)
    }

    @Test
    fun `deleteTransaction removes the entry`() = runTest {
        val tx = Transaction(
            id = "tx2",
            amount = 20.0,
            category = TransactionCategory.TRANSPORT,
            description = "Bus",
            date = LocalDate(2026, 4, 1),
            isExpense = true
        )
        repo.addTransaction(tx)
        repo.deleteTransaction("tx2")

        val results = repo.getTransactions("2026-04").first()
        assertTrue(results.isEmpty())
    }

    @Test
    fun `getTotalSpent sums expense amounts for monthYear`() = runTest {
        repo.addTransaction(Transaction("t1", 30.0, TransactionCategory.FOOD, "a", LocalDate(2026, 4, 1), isExpense = true))
        repo.addTransaction(Transaction("t2", 20.0, TransactionCategory.FOOD, "b", LocalDate(2026, 4, 2), isExpense = true))
        repo.addTransaction(Transaction("t3", 100.0, TransactionCategory.INCOME, "salary", LocalDate(2026, 4, 1), isExpense = false))

        val total = repo.getTotalSpent("2026-04")
        assertEquals(50.0, total, 0.001)
    }

    @Test
    fun `getTransactions does not return transactions from other months`() = runTest {
        repo.addTransaction(Transaction("m1", 10.0, TransactionCategory.FOOD, "Apr", LocalDate(2026, 4, 1), isExpense = true))
        repo.addTransaction(Transaction("m2", 10.0, TransactionCategory.FOOD, "Mar", LocalDate(2026, 3, 1), isExpense = true))

        val apr = repo.getTransactions("2026-04").first()
        val mar = repo.getTransactions("2026-03").first()

        assertEquals(1, apr.size)
        assertEquals(1, mar.size)
        assertEquals("m1", apr.first().id)
        assertEquals("m2", mar.first().id)
    }
}
