import SwiftUI

struct TransactionView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var amount = ""
    @State private var description = ""
    @State private var isExpense = true
    @State private var selectedCategory = "Other"
    @State private var showXpAnimation = false
    
    let categories = [
        ("🍕", "Food"), ("🚗", "Transport"), ("🛍️", "Shopping"),
        ("🎮", "Entertainment"), ("💡", "Bills"), ("💊", "Health"),
        ("📚", "Education"), ("💰", "Savings"), ("💵", "Income"), ("📦", "Other")
    ]
    
    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: BQTheme.Spacing.xl) {
                    // Type toggle
                    Picker("Type", selection: $isExpense) {
                        Text("Expense").tag(true)
                        Text("Income").tag(false)
                    }
                    .pickerStyle(.segmented)
                    
                    // Amount
                    HStack {
                        Text("$")
                            .font(.title)
                            .fontWeight(.bold)
                        TextField("0.00", text: $amount)
                            .font(.system(size: 36, weight: .bold))
                            .keyboardType(.decimalPad)
                    }
                    .padding()
                    .background(Color(.secondarySystemGroupedBackground))
                    .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                    
                    // Description
                    TextField("Description (optional)", text: $description)
                        .padding()
                        .background(Color(.secondarySystemGroupedBackground))
                        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                    
                    // Category grid
                    Text("Category")
                        .font(.headline)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    LazyVGrid(columns: Array(repeating: GridItem(.flexible(), spacing: 8), count: 3), spacing: 8) {
                        ForEach(categories, id: \.1) { emoji, name in
                            VStack(spacing: 4) {
                                Text(emoji).font(.title)
                                Text(name).font(.caption2)
                            }
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 12)
                            .background(
                                selectedCategory == name
                                ? BQTheme.Colors.primaryGreen.opacity(0.15)
                                : Color(.secondarySystemGroupedBackground)
                            )
                            .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.md))
                            .onTapGesture { selectedCategory = name }
                        }
                    }
                    
                    Spacer()
                    
                    // XP animation
                    if showXpAnimation {
                        HStack {
                            Text("⚡").font(.title2)
                            Text("+10 XP earned!")
                                .font(.headline)
                                .foregroundColor(BQTheme.Colors.accentGold)
                        }
                        .padding()
                        .background(BQTheme.Colors.accentGold.opacity(0.15))
                        .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                        .transition(.scale.combined(with: .opacity))
                    }
                    
                    // Save button
                    Button {
                        withAnimation(.spring()) {
                            showXpAnimation = true
                        }
                        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                            dismiss()
                        }
                    } label: {
                        Text("Save Transaction")
                            .font(.headline)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(isExpense ? BQTheme.Colors.accentRed : BQTheme.Colors.primaryGreen)
                            .foregroundColor(.white)
                            .clipShape(RoundedRectangle(cornerRadius: BQTheme.Radius.lg))
                    }
                    .disabled(amount.isEmpty)
                }
                .padding(BQTheme.Spacing.xl)
            }
            .navigationTitle("Add Transaction")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Cancel") { dismiss() }
                }
            }
        }
    }
}
