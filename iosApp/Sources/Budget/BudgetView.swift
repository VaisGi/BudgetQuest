import SwiftUI

struct BudgetView: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: BQTheme.Spacing.lg) {
                    // Overall progress
                    VStack(alignment: .leading, spacing: BQTheme.Spacing.sm) {
                        Text("Monthly Overview")
                            .font(.headline)
                        ProgressView(value: 0.62)
                            .tint(BQTheme.Colors.accentGold)
                            .scaleEffect(y: 2)
                        HStack {
                            Text("$1,250 spent").font(.subheadline)
                            Spacer()
                            Text("$2,000 budget")
                                .font(.subheadline)
                                .foregroundColor(BQTheme.Colors.primaryGreen)
                        }
                    }
                    .padding(BQTheme.Spacing.xl)
                    .background(Color(.secondarySystemGroupedBackground))
                    .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.xl))
                    
                    // Budget items
                    budgetItem(emoji: "🍕", name: "Food & Dining", spent: 320, limit: 500, percent: 0.64)
                    budgetItem(emoji: "🚗", name: "Transport", spent: 180, limit: 200, percent: 0.9)
                    budgetItem(emoji: "🛍️", name: "Shopping", spent: 95, limit: 300, percent: 0.32)
                    budgetItem(emoji: "🎮", name: "Entertainment", spent: 60, limit: 150, percent: 0.4)
                    budgetItem(emoji: "💡", name: "Bills", spent: 450, limit: 500, percent: 0.9)
                }
                .padding(.horizontal, BQTheme.Spacing.xl)
            }
            .navigationTitle("Budgets 📊")
        }
    }
    
    private func budgetItem(emoji: String, name: String, spent: Double, limit: Double, percent: Float) -> some View {
        let color: Color = percent > 0.8 ? BQTheme.Colors.accentOrange :
                           percent > 0.5 ? BQTheme.Colors.accentGold : BQTheme.Colors.primaryGreen
        
        return VStack(alignment: .leading, spacing: BQTheme.Spacing.md) {
            HStack {
                HStack(spacing: 12) {
                    Text(emoji).font(.title2)
                    VStack(alignment: .leading) {
                        Text(name).font(.subheadline).fontWeight(.semibold)
                        Text("$\(String(format: "%.0f", limit - spent)) remaining")
                            .font(.caption)
                            .foregroundColor(color)
                    }
                }
                Spacer()
                Text("$\(String(format: "%.0f", spent)) / $\(String(format: "%.0f", limit))")
                    .font(.caption)
                    .fontWeight(.semibold)
                    .foregroundColor(color)
            }
            ProgressView(value: Double(percent))
                .tint(color)
        }
        .padding(BQTheme.Spacing.lg)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
    }
}
