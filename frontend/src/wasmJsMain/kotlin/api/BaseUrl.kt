import kotlinx.browser.window

val baseUrl: String
    get() = if (window.location.hostname == "localhost")
        "http://localhost:8080"
    else "${window.location.protocol}//${window.location.host}"

const val apiPrefix = "/mars-landing"