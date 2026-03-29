import SwiftUI

struct QuestView: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: BQTheme.Spacing.md) {
                    questCard(emoji: "☕", title: "No Coffee Week", desc: "Skip coffee purchases for 7 days", progress: 0.43, xp: 150)
                    questCard(emoji: "🥪", title: "Pack Lunch Pro", desc: "Pack your own lunch 5 days", progress: 0.6, xp: 100)
                    questCard(emoji: "📊", title: "Daily Logger", desc: "Log at least 1 transaction daily", progress: 0.85, xp: 75)
                    questCard(emoji: "🎯", title: "Fifty Saver", desc: "Put $50 towards savings", progress: 0.0, xp: 120)
                    questCard(emoji: "🛡️", title: "Budget Keeper", desc: "Stay under budget 3 days", progress: 0.33, xp: 200)
                }
                .padding(.horizontal, BQTheme.Spacing.xl)
            }
            .navigationTitle("Quests ⚔️")
        }
    }
    
    private func questCard(emoji: String, title: String, desc: String, progress: Float, xp: Int) -> some View {
        VStack(alignment: .leading, spacing: BQTheme.Spacing.md) {
            HStack {
                HStack(spacing: 12) {
                    Text(emoji).font(.largeTitle)
                    VStack(alignment: .leading) {
                        Text(title)
                            .font(.headline)
                        Text(desc)
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }
                Spacer()
                Text("+\(xp) XP")
                    .font(.caption)
                    .fontWeight(.bold)
                    .foregroundColor(BQTheme.Colors.accentGold)
                    .padding(.horizontal, 10)
                    .padding(.vertical, 4)
                    .background(BQTheme.Colors.accentGold.opacity(0.15))
                    .clipShape(Capsule())
            }
            
            ProgressView(value: Double(progress))
                .tint(progress >= 1.0 ? BQTheme.Colors.primaryGreen : BQTheme.Colors.accentPurple)
            
            Text(progress >= 1.0 ? "✅ Completed!" : "\(Int(progress * 100))% complete")
                .font(.caption)
                .foregroundColor(progress >= 1.0 ? BQTheme.Colors.primaryGreen : .secondary)
        }
        .padding(BQTheme.Spacing.xl)
        .background(
            progress >= 1.0
            ? BQTheme.Colors.primaryGreen.opacity(0.08)
            : Color(.secondarySystemGroupedBackground)
        )
        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.xl))
    }
}
