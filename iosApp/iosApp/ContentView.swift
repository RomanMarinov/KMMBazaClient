import UIKit
import SwiftUI
import ComposeApp



struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
       // MainViewControllerKt.MainViewController2()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(edges: .all) // Add this line
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}
