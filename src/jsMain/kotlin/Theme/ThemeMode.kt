package Theme

enum class ThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromValue(value: String) =
            values().firstOrNull { it.value == value } ?: error("No Theme found for [$value]")
    }
}
