package syh.year2023.day20

import syh.AbstractAocDay
import syh.calculateLCM

class Puzzle20 : AbstractAocDay(2023, 20) {
    override fun doA(file: String): Long {
        val lines = readSingleLineFile(file)

        val flipFlopModules = mutableListOf<FlipFlopModule>()
        val conjunctionModules = mutableListOf<ConjunctionModule>()
        var broadcaster: BroadcastModule? = null

        for (line in lines) {
            if (line.startsWith("%")) {
                // flip flop
                val (name, destinations) = line.split(" -> ")
                flipFlopModules.add(FlipFlopModule(name.removePrefix("%"), destinations = destinations.split(", ")))

            } else if (line.startsWith("&")) {
                val (name, destinations) = line.split(" -> ")
                conjunctionModules.add(ConjunctionModule(name.removePrefix("&"), destinations = destinations.split(", ")))

            } else {
                val (name, destinations) = line.split(" -> ")
                broadcaster = BroadcastModule(name.removePrefix("%"), destinations = destinations.split(", "))
            }
        }
        if (broadcaster == null) {
            throw IllegalStateException("did not find broadcaster")
        }


        val allModules: MutableList<SignalReceiver> = mutableListOf()
        allModules.add(broadcaster)
        allModules.addAll(flipFlopModules)
        allModules.addAll(conjunctionModules)

        val sinks = mutableSetOf<SignalSink>()
        for (module in allModules) {
            for (destination in module.destinations) {
                val destinationModule = allModules.firstOrNull { it.name == destination }
                if (destinationModule == null) {
                    val sink = SignalSink(destination)
                    sinks.add(sink)
                } else if (destinationModule is ConjunctionModule) {
                    destinationModule.recentInputSignals[module.name] = Pulse.LOW
                }
            }
        }
        allModules.addAll(sinks)

        allModules.forEach { println(it) }

        var totalLowPulses = 0L
        var totalHighPulses = 0L

        for (i in 1..1000) {
            val (tempLow, tempHigh) = pressButton(broadcaster, allModules)
            totalLowPulses += tempLow
            totalHighPulses += tempHigh
        }


        println("total low pulses = $totalLowPulses")
        println("total high pulses = $totalHighPulses")
        println("Multiplication: ${totalLowPulses * totalHighPulses}")
        println("Addition: ${totalLowPulses + totalHighPulses}")
        return totalLowPulses * totalHighPulses
    }

    override fun doB(file: String): Long {
        val lines = readSingleLineFile(file)
        return part2(lines)
    }


    private fun pressButton(broadcaster: BroadcastModule, allModules: MutableList<SignalReceiver>): Pair<Long, Long> {

        var totalLowPulses = 0L
        var totalHighPulses = 0L

        val signalsToHandle = mutableListOf<Signal>()
        val startSignals = broadcaster.receiveLowSignal("button")
        startSignals.forEach { signalsToHandle.add(it) }
        totalLowPulses++


        while (signalsToHandle.isNotEmpty()) {
            val currentSignal = signalsToHandle.first()
            signalsToHandle.remove(currentSignal)

//        println("${currentSignal.source} -${currentSignal.pulse.name} ${currentSignal.destination}")

            val destinationModule = allModules.first { it.name == currentSignal.destination }

            when (currentSignal.pulse) {
                Pulse.LOW -> {
                    totalLowPulses++
                    val newSignals = destinationModule.receiveLowSignal(currentSignal.source)
                    newSignals.forEach { signalsToHandle.add(it) }
                }

                Pulse.HIGH -> {
                    totalHighPulses++
                    val newSignals = destinationModule.receiveHighSignal(currentSignal.source)
                    newSignals.forEach { signalsToHandle.add(it) }
                }
            }
        }
        return Pair(totalLowPulses, totalHighPulses)
    }

    private fun parseModule(input: String): Module {
        val sides = input.split(" -> ")
        val type = sides[0][0]
        val label = if (type == 'b') sides[0] else sides[0].drop(1)
        val children = sides[1].split(", ")
        return Module(label, type, "low", children, mutableListOf())
    }

    private fun connectChildren(modules: List<Module>) {
        for (module in modules) {
            for (child in module.children) {
                val node = modules.find { it.label == child }
                node?.parents?.add(Pair(module.label, "low"))
            }
        }
    }

    private fun pruneChildren(modules: List<Module>, keep: String): List<Module> {
        val broadcaster = modules.find { it.label == "broadcaster" }!!
        val newBroadcaster = Module(broadcaster.label, 'b', "low", listOf(keep), mutableListOf())
        val newModules = mutableSetOf(newBroadcaster)
        var count = 0
        while (count != newModules.size) {
            count = newModules.size
            for (module in newModules.toList()) {
                for (c in module.children) {
                    newModules.add(modules.find { it.label == c }!!)
                }
            }
        }
        return newModules.toList()
    }

    fun part2(input: List<String>): Long {
        val allModules = input.map { parseModule(it) }
            .plus(Module("output", 'o', "low", emptyList(), mutableListOf()))
            .plus(Module("rx", 'o', "low", emptyList(), mutableListOf()))

        val broadcaster = allModules.find { it.label == "broadcaster" }!!
        // broadcaster -> fm, hv, kc, bv
        val lcms = mutableListOf<Long>()
        for (cycle in broadcaster.children) {
            val modules = pruneChildren(allModules, cycle)
            for (module in modules) {
                module.parents.clear()
            }
            connectChildren(modules)

            var count = 0L
            var found = false
            while (!found) {
                count++
                val active = mutableListOf(Triple("button", "broadcaster", "low"))
                var rx = 0
                while (active.isNotEmpty()) {
                    val currentSignal = active.removeFirst()
                    if (currentSignal.second == "rx" && currentSignal.third == "low")
                        rx = 1
                    val currentModule = modules.find { it.label == currentSignal.second }!!
                    when (currentModule.type) {
                        '%' -> {
                            if (currentSignal.third == "low") {
                                currentModule.state = if (currentModule.state == "low") "high" else "low"
                                for (child in currentModule.children) {
                                    active.add(Triple(currentModule.label, child, currentModule.state))
                                }
                            }
                        }

                        '&' -> {
                            val parent = currentModule.parents.find { it.first == currentSignal.first }!!
                            currentModule.parents.remove(parent)
                            currentModule.parents.add(Pair(parent.first, currentSignal.third))
                            val signalOut = if (currentModule.parents.any { it.second == "low" }) "high" else "low"
                            for (child in currentModule.children) {
                                active.add(Triple(currentModule.label, child, signalOut))
                            }
                        }

                        'b' -> {
                            for (child in currentModule.children) {
                                active.add(Triple(currentModule.label, child, currentSignal.third))
                            }
                        }
                    }
                }
                if (rx == 1) {
                    lcms.add(count)
                    found = true
                }
            }
        }
        return calculateLCM(lcms)
    }

    data class Module(
        val label: String,
        val type: Char,
        var state: String,
        var children: List<String>,
        val parents: MutableList<Pair<String, String>>
    )

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

        private fun currentSignal(): Pulse {
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
}