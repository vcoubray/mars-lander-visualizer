package fr.vco.genetic.algorithm.visualizer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform