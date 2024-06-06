import SwiftUI
import ComposeApp
//import FirebaseCore
//import FirebaseMessaging
//
//class AppDelegate: NSObject, UIApplicationDelegate {
//  func application(_ application: UIApplication,
//                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//      FirebaseApp.configure() //important
//
//      NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos.shared)
//      
//    return true
//  }
//  func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
//        Messaging.messaging().apnsToken = deviceToken
//  }
//    
//}


/////////////////////////////


@main
struct iOSApp: App {
    
    init() {
        MainViewControllerKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
