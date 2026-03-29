package com.budgetquest.android

import android.app.Application
import com.budgetquest.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BudgetQuestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BudgetQuestApp)
            modules(sharedModule)
        }
    }
}
