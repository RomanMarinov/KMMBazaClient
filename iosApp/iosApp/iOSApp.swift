import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseMessaging


import Foundation
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import UserNotificationsUI
import SwiftUI

//
 class AppDelegate: NSObject, UIApplicationDelegate {

   func application(_ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

       FirebaseApp.configure() //important

       //By default showPushNotification value is true.
       //When set showPushNotification to false foreground push  notification will not be shown.
       //You can still get notification content using #onPushNotification listener method.
       NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: true))

     return true
   }

     internal func application(_ application: UIApplication, 
                              didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
         Messaging.messaging().apnsToken = deviceToken
   }

 }



// class AppDelegate: NSObject, UIApplicationDelegate {
//
//   func application(_ application: UIApplication,
//                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//       FirebaseApp.configure()
//
//       AppInitializer.shared.onApplicationStart()
//
//     return true
//   }
//
//     func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
//         Messaging.messaging().apnsToken = deviceToken
//     }
//
//
//     func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
//         NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
//         return UIBackgroundFetchResult.newData
//     }
//
// }


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
