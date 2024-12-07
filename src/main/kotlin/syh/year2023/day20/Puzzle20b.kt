package syh.year2023.day20

import syh.calculateLCM
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


    val endModuleFillers = allModules.filter { it.destinations.contains("hf") }

    val endModuleFillerHighMap: MutableMap<String, Boolean> =
        endModuleFillers.associate { it.name to false }.toMutableMap()
    val endModuleFillerCountMap = endModuleFillers.associate { it.name to 0L }.toMutableMap()

    loopButtonPressesUntilFilled(broadcaster, allModules, endModuleFillerHighMap, endModuleFillerCountMap)

    println("test")

    val numbers = endModuleFillerCountMap.values.toList()

    println("calculating lcm for numbers: $numbers")

    val lcm = calculateLCM(numbers)

    println("total necessary pulses = ${endModuleFillerCountMap.values}")
    println("lcm = $lcm")
}

private fun loopButtonPressesUntilFilled(
    broadcaster: BroadcastModule,
    allModules: MutableList<SignalReceiver>,
    endModuleFillerHighMap: MutableMap<String, Boolean>,
    endModuleFillerCountMap: MutableMap<String, Long>
): Pair<Long, Long> {

    var totalLowPulses = 0L
    var totalHighPulses = 0L
    var totalPulses = 0L


    while (endModuleFillerHighMap.any { !it.value }) {
        endModuleFillerHighMap.forEach { println(it) }
        println()

        val signalsToHandle = mutableListOf<Signal>()
        val startSignals = broadcaster.receiveLowSignal("button")
        startSignals.forEach { signalsToHandle.add(it) }

        totalLowPulses++
        totalPulses++


        while (signalsToHandle.isNotEmpty()) {
            val currentSignal = signalsToHandle.first()
            signalsToHandle.remove(currentSignal)

            val destinationModule = allModules.first { it.name == currentSignal.destination }

            when (currentSignal.pulse) {
                Pulse.LOW -> {
                    totalPulses++
                    totalLowPulses++
                    val newSignals = destinationModule.receiveLowSignal(currentSignal.source)
                    newSignals.forEach { signalsToHandle.add(it) }
                }

                Pulse.HIGH -> {
                    totalPulses++
                    totalHighPulses++
                    val newSignals = destinationModule.receiveHighSignal(currentSignal.source)
                    newSignals.forEach { signalsToHandle.add(it) }
                }
            }

            if (currentSignal.destination == "hf") {
                allModules.filter { it.destinations.contains("hf") }
                    .map { it as ConjunctionModule }
                    .filter { it.currentSignal() == Pulse.HIGH }
                    .filter { !endModuleFillerHighMap[it.name]!! }
                    .forEach { endParent ->
                        println("found high signal for parent ${endParent.name} on count $totalPulses")
                        endModuleFillerHighMap[endParent.name] = true
                        endModuleFillerCountMap[endParent.name] = totalPulses
                    }
            }
        }

    }

    return Pair(totalLowPulses, totalHighPulses)
}