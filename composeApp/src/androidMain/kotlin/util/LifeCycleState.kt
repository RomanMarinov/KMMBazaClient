package util

enum class LifeCycleState(name: String) {
    ON_START("ON_START"),
    ON_RESUME("ON_RESUME"),
    ON_STOP("ON_STOP"),
    ON_DESTROY("ON_DESTROY")
}