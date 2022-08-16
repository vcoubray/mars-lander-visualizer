package condigame

data class Action(val rotate: Int, val power: Int) {
    companion object {
        fun generate() = Action(
            (-15..15).random(),
            (-1..1).random()
        )
    }

    override fun toString() = "$rotate $power"
}