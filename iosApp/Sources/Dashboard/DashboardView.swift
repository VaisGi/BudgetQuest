import SwiftUI

// MARK: - Dashboard View
// When KMP framework is linked, this will observe DashboardViewModel.state via SKIE
// For now, uses local @State placeholders matching the DashboardConfig structure

struct DashboardView: View {
    @State private var greeting = "Let's crush it! 💪"
    @State private var streakDays = 5
    @State private var level = 3
    @State private var xpProgress: Float = 0.65
    @State private var totalXp = 2150
    @State private var todaySpent = 42.50
    @State private var monthlySpent = 1250.0
    @State private var monthlyBudget = 2000.0
    @State private var showAddTransaction = false
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: BQTheme.Spacing.lg) {
                    // Greeting + Level
                    greetingSection
                    
                    // Streak Card
                    streakCard
                    
                    // Spending Overview
                    spendingCard
                    
                    // Active Quests
                    sectionHeader(title: "Active Quests ⚔️")
                    questCarousel
                    
                    // Budget Snapshot
                    sectionHeader(title: "Budget Snapshot 📊")
                    budgetList
                    
                    // Premium Banner
                    premiumBanner
                }
                .padding(.horizontal, BQTheme.Spacing.xl)
                .padding(.bottom, 80)
            }
            .navigationTitle("")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        showAddTransaction = true
                    } label: {
                        Image(systemName: "plus.circle.fill")
                            .font(.title2)
                            .foregroundColor(BQTheme.Colors.primaryGreen)
                    }
                }
            }
            .sheet(isPresented: $showAddTransaction) {
                TransactionView()
            }
        }
    }
    
    // MARK: - Greeting Section
    private var greetingSection: some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.md) {
            Text(greeting)
                .font(.title2)
                .fontWeight(.bold)
            
            HStack(spacing: BQTheme.Spacing.md) {
                // Level badge
                HStack(spacing: 6) {
                    Image(systemName: "star.fill")
                        .font(.caption)
                        .foregroundColor(BQTheme.Colors.accentPurple)
                    Text("Level \(level)")
                        .font(.caption)
                        .fontWeight(.semibold)
                        .foregroundColor(BQTheme.Colors.accentPurple)
                }
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(BQTheme.Colors.accentPurple.opacity(0.15))
                .clipShape(Capsule())
                
                // XP bar
                VStack(alignment: .leading, spacing: 2) {
                    ProgressView(value: Double(xpProgress))
                        .tint(BQTheme.Colors.accentPurple)
                    Text("\(totalXp) XP")
                        .font(.caption2)
                        .foregroundColor(.secondary)
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.top, BQTheme.Spacing.lg)
    }
    
    // MARK: - Streak Card
    private var streakCard: some View {
        HStack(spacing: BQTheme.Spacing.md) {
            Text("🔥")
                .font(.largeTitle)
            VStack(alignment: .leading) {
                Text("\(streakDays) Day Streak!")
                    .font(.headline)
                    .foregroundColor(BQTheme.Colors.accentOrange)
                Text("Keep logging to maintain your streak")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
            Spacer()
        }
        .padding(BQTheme.Spacing.lg)
        .background(BQTheme.Colors.accentGold.opacity(0.12))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
    }
    
    // MARK: - Spending Card
    private var spendingCard: some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.md) {
            Text("Today's Spending")
                .font(.subheadline)
                .foregroundColor(.secondary)
            Text("$\(String(format: "%.2f", todaySpent))")
                .font(.system(size: 36, weight: .bold))
            
            HStack {
                VStack(alignment: .leading) {
                    Text("Monthly")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text("$\(String(format: "%.0f", monthlySpent))")
                        .font(.headline)
                }
                Spacer()
                VStack(alignment: .trailing) {
                    Text("Budget")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    Text("$\(String(format: "%.0f", monthlyBudget))")
                        .font(.headline)
                        .foregroundColor(BQTheme.Colors.primaryGreen)
                }
            }
        }
        .padding(BQTheme.Spacing.xl)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.xl))
    }
    
    // MARK: - Section Header
    private func sectionHeader(title: String) -> some View {
        HStack {
            Text(title)
                .font(.headline)
            Spacer()
            Button("View All") {}
                .font(.subheadline)
                .foregroundColor(BQTheme.Colors.accentPurple)
        }
    }
    
    // MARK: - Quest Carousel
    private var questCarousel: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: BQTheme.Spacing.md) {
                questMiniCard(emoji: "☕", title: "No Coffee Week", progress: 0.4, xp: 150)
                questMiniCard(emoji: "🥪", title: "Pack Lunch Pro", progress: 0.6, xp: 100)
                questMiniCard(emoji: "📊", title: "Daily Logger", progress: 0.85, xp: 75)
            }
        }
    }
    
    private func questMiniCard(emoji: String, title: String, progress: Float, xp: Int) -> some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.sm) {
            HStack(spacing: 8) {
                Text(emoji).font(.title2)
                Text(title)
                    .font(.caption)
                    .fontWeight(.semibold)
                    .lineLimit(1)
            }
            ProgressView(value: Double(progress))
                .tint(BQTheme.Colors.accentPurple)
            Text("+\(xp) XP")
                .font(.caption2)
                .foregroundColor(BQTheme.Colors.accentGold)
        }
        .padding(BQTheme.Spacing.lg)
        .frame(width: 200)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
    }
    
    // MARK: - Budget List
    private var budgetList: some View {
        VStack(spacing: BQTheme.Spacing.sm) {
            budgetRow(emoji: "🍕", name: "Food & Dining", spent: 320, limit: 500, percent: 0.64)
            budgetRow(emoji: "🚗", name: "Transport", spent: 180, limit: 200, percent: 0.9)
            budgetRow(emoji: "🛍️", name: "Shopping", spent: 95, limit: 300, percent: 0.32)
        }
    }
    
    private func budgetRow(emoji: String, name: String, spent: Double, limit: Double, percent: Float) -> some View {
        let color: Color = percent > 0.8 ? BQTheme.Colors.accentOrange :
                           percent > 0.5 ? BQTheme.Colors.accentGold : BQTheme.Colors.primaryGreen
        
        return HStack(spacing: BQTheme.Spacing.md) {
            Text(emoji).font(.title2)
            VStack(alignment: .leading, spacing: 4) {
                Text(name).font(.subheadline).fontWeight(.medium)
                ProgressView(value: Double(percent))
                    .tint(color)
            }
            VStack(alignment: .trailing) {
                Text("$\(String(format: "%.0f", spent))")
                    .font(.subheadline)
                    .fontWeight(.bold)
                    .foregroundColor(color)
                Text("of $\(String(format: "%.0f", limit))")
                    .font(.caption2)
                    .foregroundColor(.secondary)
            }
        }
        .padding(BQTheme.Spacing.lg)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.md))
    }
    
    // MARK: - Premium Banner
    private var premiumBanner: some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.md) {
            Text("⭐ Unlock Premium")
                .font(.headline)
                .foregroundColor(.white)
            Text("Unlimited goals, bank sync, AI insights")
                .font(.subheadline)
                .foregroundColor(.white.opacity(0.8))
            Button("Start Free Trial") {}
                .font(.subheadline)
                .fontWeight(.bold)
                .padding(.horizontal, 20)
                .padding(.vertical, 10)
                .background(BQTheme.Colors.accentGold)
                .foregroundColor(.black)
                .clipShape(Capsule())
        }
        .padding(BQTheme.Spacing.xl)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            LinearGradient(
                colors: [BQTheme.Colors.accentPurple, BQTheme.Colors.accentPurpleDark],
                startPoint: .leading,
                endPoint: .trailing
            )
        )
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.xl))
    }
}
