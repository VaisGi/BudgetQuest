import SwiftUI

struct SavingsView: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: BQTheme.Spacing.lg) {
                    savingsGoalCard(
                        emoji: "🏖️",
                        name: "Vacation Fund",
                        saved: 1200,
                        target: 3000,
                        progress: 0.4,
                        milestone: "🌿 25% There!"
                    )
                    
                    savingsGoalCard(
                        emoji: "🚗",
                        name: "New Car",
                        saved: 8500,
                        target: 15000,
                        progress: 0.57,
                        milestone: "🌳 Halfway!"
                    )
                }
                .padding(.horizontal, BQTheme.Spacing.xl)
            }
            .navigationTitle("Savings Goals 💰")
        }
    }
    
    private func savingsGoalCard(emoji: String, name: String, saved: Double, target: Double, progress: Float, milestone: String) -> some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.lg) {
            HStack(spacing: 12) {
                Text(emoji).font(.largeTitle)
                VStack(alignment: .leading) {
                    Text(name)
                        .font(.headline)
                    Text(milestone)
                        .font(.caption)
                        .foregroundColor(BQTheme.Colors.accentGold)
                }
            }
            
            ProgressView(value: Double(progress))
                .tint(BQTheme.Colors.primaryGreen)
                .scaleEffect(y: 2)
            
            HStack {
                Text("$\(String(format: "%.0f", saved)) saved")
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .foregroundColor(BQTheme.Colors.primaryGreen)
                Spacer()
                Text("of $\(String(format: "%.0f", target))")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            
            HStack(spacing: BQTheme.Spacing.sm) {
                ForEach([10, 25, 50], id: \.self) { amount in
                    Button("+$\(amount)") {}
                        .font(.caption)
                        .fontWeight(.medium)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 8)
                        .background(Color(.secondarySystemFill))
                        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.md))
                        .frame(maxWidth: .infinity)
                }
            }
        }
        .padding(BQTheme.Spacing.xl)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.xl))
    }
}
