import SwiftUI

struct OnboardingView: View {
    let onComplete: () -> Void
    
    @State private var currentStep = 0
    @State private var selectedGoal: String? = nil
    @State private var selectedIncome: String? = nil
    @State private var userName = ""
    
    let totalSteps = 4
    
    let goals = [
        ("💰", "Save Money", "Build an emergency fund or save for something special"),
        ("📊", "Track Budget", "Know where your money goes and stick to limits"),
        ("🎯", "Pay Off Debt", "Snowball your way out of debt with a plan"),
        ("🚀", "All of the Above", "Master your complete financial picture")
    ]
    
    let incomeRanges = [
        "Under $2,000/mo", "$2,000 – $4,000/mo", "$4,000 – $7,000/mo",
        "$7,000 – $10,000/mo", "$10,000+/mo", "Prefer not to say"
    ]
    
    var body: some View {
        VStack(spacing: 0) {
            Spacer().frame(height: 60)
            
            // Progress dots
            HStack(spacing: 8) {
                ForEach(0..<totalSteps, id: \.self) { index in
                    RoundedRectangle(cornerRadius: 4)
                        .fill(index <= currentStep ? BQTheme.Colors.accentPurple : BQTheme.Colors.accentPurple.opacity(0.2))
                        .frame(width: index == currentStep ? 24 : 8, height: 8)
                        .animation(.spring(), value: currentStep)
                }
            }
            
            Spacer().frame(height: 40)
            
            // Step content
            TabView(selection: $currentStep) {
                welcomeStep.tag(0)
                goalStep.tag(1)
                incomeStep.tag(2)
                nameStep.tag(3)
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
            
            // Navigation
            HStack {
                if currentStep > 0 {
                    Button("Back") {
                        withAnimation { currentStep -= 1 }
                    }
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
                    .background(Color(.secondarySystemFill))
                    .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                } else {
                    Spacer()
                }
                
                Spacer()
                
                Button(currentStep == totalSteps - 1 ? "Let's Go! 🚀" : "Next") {
                    if currentStep == totalSteps - 1 {
                        onComplete()
                    } else {
                        withAnimation { currentStep += 1 }
                    }
                }
                .fontWeight(.bold)
                .padding(.horizontal, 24)
                .padding(.vertical, 12)
                .background(BQTheme.Colors.accentPurple)
                .foregroundColor(.white)
                .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
            }
            .padding(.horizontal, BQTheme.Spacing.xl)
            .padding(.bottom, BQTheme.Spacing.xxl)
        }
    }
    
    private var welcomeStep: some View {
        VStack(spacing: BQTheme.Spacing.xxl) {
            Spacer()
            Text("🎮").font(.system(size: 80))
            Text("Welcome to\nBudgetQuest")
                .font(.largeTitle)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)
            Text("Turn your finances into an adventure.\nEarn XP, complete quests, level up!")
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            Spacer()
        }
        .padding(.horizontal, BQTheme.Spacing.xl)
    }
    
    private var goalStep: some View {
        VStack(spacing: BQTheme.Spacing.xxl) {
            Text("What's your #1\nfinancial goal?")
                .font(.title)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)
            
            VStack(spacing: BQTheme.Spacing.md) {
                ForEach(goals, id: \.1) { emoji, name, desc in
                    HStack(spacing: BQTheme.Spacing.lg) {
                        Text(emoji).font(.title)
                        VStack(alignment: .leading) {
                            Text(name).font(.subheadline).fontWeight(.semibold)
                            Text(desc).font(.caption).foregroundColor(.secondary)
                        }
                        Spacer()
                    }
                    .padding(BQTheme.Spacing.lg)
                    .background(
                        selectedGoal == name
                        ? BQTheme.Colors.accentPurple.opacity(0.12)
                        : Color(.secondarySystemGroupedBackground)
                    )
                    .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                    .onTapGesture { selectedGoal = name }
                }
            }
            Spacer()
        }
        .padding(.horizontal, BQTheme.Spacing.xl)
    }
    
    private var incomeStep: some View {
        VStack(spacing: BQTheme.Spacing.xxl) {
            Text("Monthly income range?")
                .font(.title)
                .fontWeight(.bold)
            Text("Helps us set realistic budgets")
                .font(.subheadline)
                .foregroundColor(.secondary)
            
            VStack(spacing: BQTheme.Spacing.sm) {
                ForEach(incomeRanges, id: \.self) { range in
                    Text(range)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(BQTheme.Spacing.lg)
                        .background(
                            selectedIncome == range
                            ? BQTheme.Colors.accentPurple.opacity(0.12)
                            : Color(.secondarySystemGroupedBackground)
                        )
                        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.md))
                        .onTapGesture { selectedIncome = range }
                }
            }
            Spacer()
        }
        .padding(.horizontal, BQTheme.Spacing.xl)
    }
    
    private var nameStep: some View {
        VStack(spacing: BQTheme.Spacing.xxl) {
            Spacer()
            Text("👋").font(.system(size: 64))
            Text("What should we\ncall you?")
                .font(.title)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)
            TextField("Your name", text: $userName)
                .font(.title3)
                .padding()
                .background(Color(.secondarySystemGroupedBackground))
                .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                .padding(.horizontal)
            Spacer()
        }
        .padding(.horizontal, BQTheme.Spacing.xl)
    }
}
