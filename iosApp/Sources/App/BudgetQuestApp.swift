import SwiftUI

@main
struct BudgetQuestApp: App {
    // TODO: Initialize Koin for iOS when KMP framework is linked
    // KoinHelper.shared.startKoin()
    
    @State private var hasCompletedOnboarding = false
    
    var body: some Scene {
        WindowGroup {
            if hasCompletedOnboarding {
                MainTabView()
            } else {
                OnboardingView(onComplete: {
                    hasCompletedOnboarding = true
                })
            }
        }
    }
}
