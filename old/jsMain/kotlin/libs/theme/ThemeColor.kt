package libs.theme

enum class ThemeColor(val value: String) {
    RED("red"),
    PINK("pink"),
    BLUE("blue"),
    GREEN("green"),
    YELLOW("yellow"),
    ORANGE("orange");

    companion object {
        fun fromValue(value: String) =
            entries.firstOrNull { it.value == value } ?: error("No Color found for [$value]")
    }
}
