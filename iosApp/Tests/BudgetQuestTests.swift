import XCTest
@testable import BudgetQuest
import shared

final class BudgetQuestTests: XCTestCase {

    func testInitialization() throws {
        // Assert that we can resolve KMP dependencies directly via the shared.xcframework
        let dashboardViewModel: DashboardViewModel? = nil // Only typed out, we test compilation
        XCTAssertNil(dashboardViewModel, "DashboardViewModel should be nil without initialization")
        
        let fakeUserRepo = FakeUserRepository()
        XCTAssertNotNil(fakeUserRepo, "FakeUserRepository should be exported to iOS target")
    }
}
