//import SwiftUI
////import ComposeApp
//import FirebaseCore
//import Firebase
//import FirebaseMessaging
//import UserNotifications
////import Foundation
//import UserNotificationsUI
//
//
//class AppDelegate: NSObject, UIApplicationDelegate {
//
//    
//  func application(_ application: UIApplication,
//                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//
//      FirebaseApp.configure() //important
//      
//              // Настройка уведомлений
//              let askPermission = true // Или false, в зависимости от ваших требований
//              NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: askPermission))
//      
//      ///////////////////
//      ///
//      ///
//      // Регистрация для удаленных уведомлений
//        //application.registerForRemoteNotifications()
//       //Устанавливает делегата текущего объекта UNUserNotificationCenter.
//             // UNUserNotificationCenter.current().delegate = self
//      
//              // Запрашивает разрешение на отправку уведомлений и выводит результат в консоль.
//              let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
//              UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: { granted, error in
//                        if granted {
//                            print("4444 Notification authorization granted")
//                        } else {
//                            print("4444 Notification authorization denied")
//                        }
//                    })
//      
////              // Регистрирует уведомления для удаленных уведомлений.
//             // application.registerForRemoteNotifications()
//      
//      /////////
//    return true
//  }
//
//
//
//    @objc(application:didRegisterForRemoteNotificationsWithDeviceToken:)
//    private func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
//        print("AppDelegate deviceToken= \(deviceToken)")
////        print("AppDelegate deviceToken= \(deviceToken.base64EncodedString())")
//        Messaging.messaging().apnsToken = deviceToken
//  }
//    
//    
//        // Метод обработки ошибки при регистрации
//        func application(_ application: UIApplication,
//                         didFailToRegisterForRemoteNotificationsWithError error: Error) {
//            print("application(_:didFailToRegisterForRemoteNotificationsWithError:) called")
//            print("Failed to register for remote notifications: \(error)")
//        }
////        // Вызывается, когда приложение получает удаленное уведомление (Push-уведомление) в фоновом режиме.
////        func application(_ application: UIApplication,
////                         didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
////            print("application(_:didReceiveRemoteNotification:) called")
////            NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
////            
////            print("AppDelegate получает удаленное уведомление= \(userInfo)")
////            
////            return UIBackgroundFetchResult.newData
////        }
//        // Вызывается, когда уведомление будет отображаться пользователю при активном приложении.
//        func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
//            print("userNotificationCenter(_:willPresent:withCompletionHandler:) called")
//            completionHandler([.banner, .list, .badge, .sound])
//        }
//}




import SwiftUI

import ComposeApp
import Foundation
import FirebaseCore
import FirebaseMessaging



class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      
      AppInitializerIos.shared.onApplicationStart()
      
      // Запрос на регистрацию уведомлений
              UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
                  print("Permission granted: \(granted)")
              }
              
              application.registerForRemoteNotifications()
      
    return true
  }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
        // Проверьте, что токен корректный: Убедитесь, что вы передаете правильный APNS токен в Firebase. Используйте следующий код для отладки:
        let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
        let token = tokenParts.joined()
        print("Device Token: \(token)")
        
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
           print("Failed to register for remote notifications: \(error.localizedDescription)")
       }
    
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
    }
    
    
}




@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
     
    
    
    
    init() {
    
        MainViewControllerKt.doInitKoin()
        
//        print("iOSApp: ->")
//            do {
//                try MainViewControllerKt.doInitKoin()
//            } catch {
//                // Обработка исключения
//                print("Error initializing Koin: \(error)")
//            }
        }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

