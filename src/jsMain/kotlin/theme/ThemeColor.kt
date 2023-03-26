package theme

enum class ThemeColor(val value: String) {
    BLUE("blue"),
    RED("red"),
    GREEN("green");

    companion object {
        fun fromValue(value: String) =
            values().firstOrNull { it.value == value } ?: error("No Color found for [$value]")
    }
}
