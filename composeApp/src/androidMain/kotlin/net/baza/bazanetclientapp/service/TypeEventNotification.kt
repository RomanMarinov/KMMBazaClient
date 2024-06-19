package net.baza.bazanetclientapp.service

enum class TypeEventNotification(val value: String) {
    CALL("call"),
    MISSED_CALL("missedcall"),
    NOTIFY_INFO("notify")
}