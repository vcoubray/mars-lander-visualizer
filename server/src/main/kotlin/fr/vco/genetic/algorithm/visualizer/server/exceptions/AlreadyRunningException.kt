package fr.vco.genetic.algorithm.visualizer.server.exceptions

import java.lang.RuntimeException

class AlreadyRunningException(message: String = "") : RuntimeException(message)