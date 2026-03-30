package com.budgetquest.presentation.dashboard

import app.cash.turbine.test
import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.usecase.CalculateBudgetUseCase
import com.budgetquest.domain.usecase.GetQuestsUseCase
import com.budgetquest.domain.usecase.TrackStreakUseCase
import com.budgetquest.testing.fake.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var fakeTransactionRepo: FakeTransactionRepository
    private lateinit var fakeBudgetRepo: FakeBudgetRepository
    private lateinit var fakeUserRepo: FakeUserRepository
    private lateinit var fakeQuestRepo: FakeQuestRepository
    private lateinit var fakeSavingsRepo: FakeSavingsGoalRepository

    private lateinit var viewModel: DashboardViewModel

    @BeforeTest
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

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest(testDispatcher) {
        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
        }
    }

    @Test
    fun `OnScreenLoaded fetches data and updates state correctly`() = runTest(testDispatcher) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val monthYear = "${today.year}-${today.monthNumber.toString().padStart(2, '0')}"

        fakeUserRepo.populateProfile(UserProfile(
            id = "1", 
            displayName = "Hero", 
            currentStreak = 5, 
            isPremium = true
        ))
        
        fakeBudgetRepo.populateBudgets(listOf(
            Budget("1", TransactionCategory.FOOD, 500.0, 0.0, monthYear)
        ))

        // Trigger Event
        viewModel.onEvent(DashboardEvent.OnScreenLoaded)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)

            // The combine operator evaluates multiple emissions. We wait for the final populated state.
            val loadedState = awaitItem()
            
            assertFalse(loadedState.isLoading)
            assertTrue(loadedState.greeting.contains("Hero"))
            assertEquals(5, loadedState.streakDays)
            assertTrue(loadedState.streakBadgeVisible)
            assertTrue(loadedState.isPremiumUser)
            assertFalse(loadedState.showUpgradePrompt)
            assertEquals(1, loadedState.budgetCards.size)
            assertEquals(500.0, loadedState.budgetCards[0].limit)
            assertEquals(0.0, loadedState.budgetCards[0].spent)
            assertEquals(TransactionCategory.FOOD.emoji, loadedState.budgetCards[0].categoryEmoji)
            assertEquals(0f, loadedState.budgetCards[0].percentUsed)
        }
    }
}
