import SwiftUI
import ComposeApp
import FirebaseCore
import Firebase
import FirebaseMessaging
import UserNotifications
import Foundation
import UserNotificationsUI

//// Класс AppDelegate отвечает за управление поведением вашего приложения и его жизненным циклом.
//class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
//
//    // Вызывается при запуске приложения и загрузке завершена. Печатает сообщение в консоль.
//    func application(_ application: UIApplication,
//                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//        print("application(_:didFinishLaunchingWithOptions:) called")
//        
//       // FirebaseApp.configure() // Важно
//
//        let askPermission = true // Или false, в зависимости от ваших требований
//        NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: askPermission))
//        
//        // Устанавливает делегата текущего объекта UNUserNotificationCenter.
//        UNUserNotificationCenter.current().delegate = self
//        
//        // Запрашивает разрешение на отправку уведомлений и выводит результат в консоль.
//        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
//        UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: { granted, error in
//                  if granted {
//                      print("4444 Notification authorization granted")
//                  } else {
//                      print("4444 Notification authorization denied")
//                  }
//              })
//        
//        // Регистрирует уведомления для удаленных уведомлений.
//        application.registerForRemoteNotifications()
//        
//        print("FCM заходит")
//        return true
//    }
//    
//    // метод получения APNS токена
//    func application(_ application: UIApplication,
//                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
//        print("application(_:didRegisterForRemoteNotificationsWithDeviceToken:) called")
//        print("FCM apnsToken token: \(deviceToken)")
//        
//        // Устанавливаем APNS токен для Firebase Messaging
//        Messaging.messaging().apnsToken = deviceToken
//        
//        // Получаем FCM токен после установки APNS токена
//        fetchFCMToken()
//    }
//    
//    // Метод обработки ошибки при регистрации
//    func application(_ application: UIApplication,
//                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
//        print("application(_:didFailToRegisterForRemoteNotificationsWithError:) called")
//        print("Failed to register for remote notifications: \(error)")
//    }
//    // Вызывается, когда приложение получает удаленное уведомление (Push-уведомление) в фоновом режиме.
//    func application(_ application: UIApplication,
//                     didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
//        print("application(_:didReceiveRemoteNotification:) called")
//        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
//        return UIBackgroundFetchResult.newData
//    }
//    // Вызывается, когда уведомление будет отображаться пользователю при активном приложении.
//    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
//        print("userNotificationCenter(_:willPresent:withCompletionHandler:) called")
//        completionHandler([.banner, .list, .badge, .sound])
//    }
//    
//    func fetchFCMToken() {
//        print("fetchFCMToken() called")
//        Messaging.messaging().token { token, error in
//            if let error = error {
//                print("Error fetching FCM registration token: \(error)")
//            } else if let token = token {
//                print("FCM registration token: \(token)")
//                // Здесь можно обновить UI или выполнить другие действия с токеном
//                // Например, self.fcmRegTokenMessage.text = "Remote FCM registration token: \(token)"
//            }
//        }
//    }
//}

class AppDelegate: NSObject, UIApplicationDelegate {

    
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

     // FirebaseApp.configure() //important
      
              // Настройка уведомлений
              let askPermission = true // Или false, в зависимости от ваших требований
              NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: askPermission))
    return true
  }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
        
        
        
        print("AppDelegate deviceToken= \(deviceToken.base64EncodedString())")
        Messaging.messaging().apnsToken = deviceToken
  }
}

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
     
    init() {
        print("iOSApp: ->")
            do {
                FirebaseApp.configure() // Важно
                
                
                
                try MainViewControllerKt.doInitKoin()
                
                
                
                
            } catch {
                // Обработка исключения
                print("Error initializing Koin: \(error)")
            }
        }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}


//
//
//class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
//
//    func application(_ application: UIApplication,
//                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//        // Инициализация Firebase
//        FirebaseApp.configure()
//        
//        // Настройка уведомлений
//        let askPermission = true // Или false, в зависимости от ваших требований
//        NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: askPermission))
//        
//        // Регистрация для удаленных уведомлений
//        application.registerForRemoteNotifications()
//        
//        // Настройка центра уведомлений
//        UNUserNotificationCenter.current().delegate = self
//        
//        return true
//    }
//    
//    func application(_ application: UIApplication,
//                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
//        Messaging.messaging().apnsToken = deviceToken
//    }
//    
//    
//    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
//            completionHandler([.banner, .list, .badge, .sound])
//        }
//    
////    // Обработка уведомлений на переднем плане
////    func userNotificationCenter(_ center: UNUserNotificationCenter,
////                                willPresent notification: UNNotification,
////                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
////        completionHandler([.alert, .badge, .sound])
////    }
//}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//class AppDelegate: NSObject, UIApplicationDelegate {
//
//func application(_ application: UIApplication,
//                 didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//
//   // FirebaseApp.configure() //important
//
//    let askPermission = true // Или false, в зависимости от ваших требований
//    NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(showPushNotification: true, askNotificationPermissionOnStart: askPermission))
//    return true
//}
//    
//    func application(_ application: UIApplication,
//                         didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Foundation.Data) {
//            Messaging.messaging().apnsToken = deviceToken
//        }
//}

//@main
//struct iOSApp: App {
//  
//    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
//     
//       
//    init() {
//        FirebaseApp.configure() // Важно
//            do {
//                try MainViewControllerKt.doInitKoin()
//            } catch {
//                // Обработка исключения
//                print("Error initializing Koin: \(error)")
//            }
//        }
//    
//    var body: some Scene {
//        WindowGroup {
//            ContentView()
//        }
//    }
//}

//
//@main
//struct iOSApp: App {
//
//    init() {
//        MainViewControllerKt.doInitKoin()
//    }
//    
//    var body: some Scene {
//        WindowGroup {
//            ContentView()
//        }
//    }
//}
