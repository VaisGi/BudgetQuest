import SwiftUI

struct MainTabView: View {
    @State private var selectedTab = 0
    
    var body: some View {
        TabView(selection: $selectedTab) {
            DashboardView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
                .tag(0)
            
            BudgetView()
                .tabItem {
                    Label("Budget", systemImage: "chart.pie.fill")
                }
                .tag(1)
            
            SavingsView()
                .tabItem {
                    Label("Savings", systemImage: "target")
                }
                .tag(2)
            
            QuestView()
                .tabItem {
                    Label("Quests", systemImage: "shield.fill")
                }
                .tag(3)
        }
        .tint(BQTheme.Colors.primaryGreen)
    }
}
