import SwiftUI

/// BudgetQuest Design System tokens for iOS
/// Mirrors the Android Material3 theme for cross-platform consistency
struct BQTheme {
    // MARK: - Colors
    struct Colors {
        static let primaryGreen = Color(hex: "00C896")
        static let primaryGreenDark = Color(hex: "00A67E")
        static let accentPurple = Color(hex: "7C4DFF")
        static let accentPurpleDark = Color(hex: "6200EA")
        static let accentGold = Color(hex: "FFAB00")
        static let accentOrange = Color(hex: "FF6D00")
        static let accentRed = Color(hex: "FF1744")
        static let accentBlue = Color(hex: "2979FF")
        
        // Dark surfaces
        static let darkSurface = Color(hex: "121218")
        static let darkSurfaceVariant = Color(hex: "1E1E2A")
        static let darkCard = Color(hex: "252536")
        
        // Light surfaces
        static let lightBackground = Color(hex: "F8F9FE")
        static let lightCard = Color(hex: "F0F2FA")
    }
    
    // MARK: - Spacing
    struct Spacing {
        static let xs: CGFloat = 4
        static let sm: CGFloat = 8
        static let md: CGFloat = 12
        static let lg: CGFloat = 16
        static let xl: CGFloat = 20
        static let xxl: CGFloat = 24
        static let xxxl: CGFloat = 32
    }
    
    // MARK: - Corner Radius
    struct Radius {
        static let sm: CGFloat = 8
        static let md: CGFloat = 12
        static let lg: CGFloat = 16
        static let xl: CGFloat = 20
        static let pill: CGFloat = 24
    }
}

// MARK: - Color Hex Extension
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 6:
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8:
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
