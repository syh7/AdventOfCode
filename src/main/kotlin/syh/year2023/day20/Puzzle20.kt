package syh.year2023.day20

import syh.readSingleLineFile


fun main() {
    val lines = readSingleLineFile("year2023/day20/actual.txt")

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