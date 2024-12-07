package syh.year2023.day20


enum class Pulse { LOW, HIGH }
enum class Status { OFF, ON }

abstract class SignalReceiver(open val name: String, open val destinations: List<String>) {
    abstract fun receiveHighSignal(source: String): List<Signal>
    abstract fun receiveLowSignal(source: String): List<Signal>
}

data class Signal(val source: String, val destination: String, val pulse: Pulse)

data class BroadcastModule(override val name: String, override val destinations: List<String>) :
    SignalReceiver(name, destinations) {
    override fun receiveHighSignal(source: String): List<Signal> {
        return destinations.map { Signal(this.name, it, Pulse.HIGH) }
    }

    override fun receiveLowSignal(source: String): List<Signal> {
        return destinations.map { Signal(this.name, it, Pulse.LOW) }
    }

}

data class FlipFlopModule(
    override val name: String, override val destinations: List<String>, var status: Status = Status.OFF
) : SignalReceiver(name, destinations) {
    override fun receiveHighSignal(source: String): List<Signal> {
        // do nothing
        return emptyList()
    }

    override fun receiveLowSignal(source: String): List<Signal> {
        if (this.status == Status.OFF) {
            status = Status.ON
            return destinations.map { Signal(this.name, it, Pulse.HIGH) }
        } else {
            status = Status.OFF
            return destinations.map { Signal(this.name, it, Pulse.LOW) }
        }
    }
}

data class ConjunctionModule(
    override val name: String,
    override val destinations: List<String>,
    val recentInputSignals: MutableMap<String, Pulse> = mutableMapOf()
) : SignalReceiver(name, destinations) {
    override fun receiveHighSignal(source: String): List<Signal> {
        recentInputSignals[source] = Pulse.HIGH
        return calculateSignals()
    }

    override fun receiveLowSignal(source: String): List<Signal> {
        recentInputSignals[source] = Pulse.LOW
        return calculateSignals()
    }

    fun currentSignal(): Pulse {
        return if (recentInputSignals.all { (_, pulse) -> pulse == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
    }

    private fun calculateSignals(): List<Signal> {
        val currentSignal = currentSignal()
        return destinations.map { Signal(this.name, it, currentSignal) }
    }
}

data class SignalSink(
    override val name: String, var receivedHigh: Boolean = false, var receivedLow: Boolean = false
) : SignalReceiver(name, emptyList()) {
    override fun receiveHighSignal(source: String): List<Signal> {
        receivedHigh = true
        return emptyList()
    }

    override fun receiveLowSignal(source: String): List<Signal> {
        receivedLow = true
        return emptyList()
    }
}