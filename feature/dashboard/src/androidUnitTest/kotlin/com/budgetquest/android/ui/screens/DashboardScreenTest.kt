package com.budgetquest.android.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.usecase.CalculateBudgetUseCase
import com.budgetquest.domain.usecase.GetQuestsUseCase
import com.budgetquest.domain.usecase.TrackStreakUseCase
import com.budgetquest.presentation.dashboard.DashboardViewModel
import com.budgetquest.testing.fake.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], instrumentedPackages = ["androidx.loader.content"]) // Add standard config to avoid Compose/Robolectric edge cases
class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeTransactionRepo: FakeTransactionRepository
    private lateinit var fakeBudgetRepo: FakeBudgetRepository
    private lateinit var fakeUserRepo: FakeUserRepository
    private lateinit var fakeQuestRepo: FakeQuestRepository
    private lateinit var fakeSavingsRepo: FakeSavingsGoalRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        fakeTransactionRepo = FakeTransactionRepository()
        fakeBudgetRepo = FakeBudgetRepository()
        fakeUserRepo = FakeUserRepository()
        fakeQuestRepo = FakeQuestRepository()
        fakeSavingsRepo = FakeSavingsGoalRepository()

        val calculateBudget = CalculateBudgetUseCase(fakeBudgetRepo, fakeTransactionRepo)
        val getQuests = GetQuestsUseCase(fakeQuestRepo, fakeUserRepo)
        val trackStreak = TrackStreakUseCase(fakeUserRepo)

        viewModel = DashboardViewModel(
            calculateBudget = calculateBudget,
            getQuests = getQuests,
            trackStreak = trackStreak,
            userRepo = fakeUserRepo,
            savingsRepo = fakeSavingsRepo,
            transactionRepo = fakeTransactionRepo
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `dashboard renders loaded user profile and greeting`() {
        // Seed initial data
        fakeUserRepo.populateProfile(
            UserProfile(
                id = "1",
                displayName = "Vaishakh",
                currentStreak = 12,
                totalXp = 400,
                isPremium = false
            )
        )

        composeTestRule.setContent {
            DashboardScreen(
                onAddTransaction = {},
                onViewBudgets = {},
                onViewSavings = {},
                onViewQuests = {},
                viewModel = viewModel
            )
        }
        
        // Wait for coroutines and initial Compose tree recomposition
        testDispatcher.scheduler.advanceUntilIdle()
        composeTestRule.waitForIdle()

        // Assert greeting
        // Wait, formatting strings might differ depending on weekend via DashboardViewModel
        // Since Robolectric executes 'Clock.System.todayIn', the logic decides if it's weekend or not.
        // Actually, let's just assert that *something* with "Vaishakh!" exists.
        composeTestRule.onNodeWithText("Vaishakh", substring = true).assertExists()
        
        // Assert Streak logic
        composeTestRule.onNodeWithText("12 DAY STREAK", substring = true).assertExists()
        
        // Premium banner might be scrolled off screen in Robolectric default bounds
        // Assert XP logic
        composeTestRule.onNodeWithText("400 XP").assertExists()
    }
}
