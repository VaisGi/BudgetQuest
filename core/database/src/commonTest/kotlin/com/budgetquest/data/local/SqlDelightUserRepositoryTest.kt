package com.budgetquest.data.local

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.budgetquest.core.database.*
import com.budgetquest.db.BudgetQuestDatabase
import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.Badges
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class SqlDelightUserRepositoryTest {

    private lateinit var repo: SqlDelightUserRepository
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
        repo = SqlDelightUserRepository(queries)
    }

    @AfterTest
    fun teardown() = driver.close()

    @Test
    fun `getUserProfile returns default profile on first access`() = runTest {
        val profile = repo.getUserProfile().first()
        assertEquals("local_user", profile.id)
        assertEquals("Quester", profile.displayName)
        assertEquals(1, profile.level)
        assertEquals(0, profile.totalXp)
    }

    @Test
    fun `createDefaultProfile overrides the existing profile`() = runTest {
        repo.createDefaultProfile("Vaishakh")
        val profile = repo.getUserProfile().first()
        assertEquals("Vaishakh", profile.displayName)
    }

    @Test
    fun `addXp increases totalXp and recalculates level`() = runTest {
        repo.addXp(600) // Level 2 threshold is 500
        val profile = repo.getUserProfile().first()
        assertEquals(600, profile.totalXp)
        assertEquals(2, profile.level)
    }

    @Test
    fun `updateStreak persists and tracks longestStreak`() = runTest {
        repo.updateStreak(5)
        repo.updateStreak(3) // longestStreak should still be 5
        val profile = repo.getUserProfile().first()
        assertEquals(3, profile.currentStreak)
        assertEquals(5, profile.longestStreak)
    }

    @Test
    fun `unlockBadge adds badge and does not duplicate`() = runTest {
        val badge = Badges.FIRST_TRANSACTION
        repo.unlockBadge(badge)
        repo.unlockBadge(badge) // duplicate call
        val profile = repo.getUserProfile().first()
        val unlocked = profile.badges.filter { it.id == badge.id && it.isUnlocked }
        assertEquals(1, unlocked.size)
    }
}
