package libs.theme

enum class ThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String) =
            entries.firstOrNull { it.value == value } ?: error("No Theme found for [$value]")
    }
}
