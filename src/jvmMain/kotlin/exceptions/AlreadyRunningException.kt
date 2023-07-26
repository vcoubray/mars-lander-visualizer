package exceptions

import java.lang.RuntimeException

class AlreadyRunningException(message: String = "") : RuntimeException(message)