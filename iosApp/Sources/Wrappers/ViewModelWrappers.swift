import Foundation

// MARK: - SKIE ViewModel Wrappers
// When the KMP shared framework is linked with SKIE, these wrappers
// will bridge Kotlin StateFlow → Swift @Published properties.
//
// SKIE automatically translates:
// - Kotlin StateFlow → Swift AsyncSequence (consumable via .values)
// - Kotlin sealed classes → Swift enums (exhaustive switch)
// - Kotlin Coroutines → Swift async/await
//
// Example wrapper pattern (uncomment when KMP framework is integrated):
//
// import shared  // KMP framework
// import Combine
//
// @MainActor
// class DashboardViewModelWrapper: ObservableObject {
//     private let viewModel: DashboardViewModel
//     @Published var config: DashboardConfig
//
//     init() {
//         self.viewModel = KoinHelper.shared.getDashboardViewModel()
//         self.config = viewModel.state.value as! DashboardConfig
//
//         Task {
//             for await state in viewModel.state {  // SKIE makes this work!
//                 self.config = state as! DashboardConfig
//             }
//         }
//     }
//
//     func onEvent(_ event: DashboardEvent) {
//         viewModel.onEvent(event: event)
//     }
// }

// Placeholder — remove when KMP framework is linked
class ViewModelWrapperPlaceholder {
    // This file serves as documentation for the SKIE integration pattern.
    // Each KMP ViewModel gets a corresponding SwiftUI wrapper that:
    // 1. Holds a reference to the Kotlin ViewModel
    // 2. Observes StateFlow via SKIE's AsyncSequence bridge
    // 3. Maps Kotlin Config → SwiftUI @Published state
    // 4. Forwards SwiftUI events → Kotlin onEvent()
}
