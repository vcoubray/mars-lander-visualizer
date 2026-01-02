package services

import java.util.concurrent.atomic.AtomicBoolean

class ServerStatusService {
    val isRunning = AtomicBoolean(false)
}