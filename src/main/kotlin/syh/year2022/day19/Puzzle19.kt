package syh.year2022.day19

import kotlin.math.max
import syh.AbstractAocDay


class Puzzle19 : AbstractAocDay(2022, 19) {

    override fun doA(file: String): String {
        val blueprints = readSingleLineFile(file).map { Blueprint.parseBlueprint(it) }
        val sum = blueprints
            .parallelStream()
            .map {
                println("starting blueprint ${it.id}")
                val score = calculateBlueprintScore(it, 24)
                println("blueprint ${it.id} had score $score")
                it.id * score
            }
            .reduce { t, u -> t + u }.get()
        return sum.toString()
    }

    override fun doB(file: String): String {
        val blueprints = readSingleLineFile(file)
            .take(3) // only first 3 matter
            .map { Blueprint.parseBlueprint(it) }
        val sum = blueprints
            .parallelStream()
            .map {
                println("starting blueprint ${it.id}")
                val score = calculateBlueprintScore(it, 32)
                println("blueprint ${it.id} had score $score")
                it.id * score
            }
            .reduce { t, u -> t + u }.get()
        return sum.toString()
    }

    private fun calculateBlueprintScore(blueprint: Blueprint, timeLeft: Int): Int {
        val beginState = State(timeLeft, mutableListOf(1, 0, 0, 0), mutableListOf(0, 0, 0, 0))
        val maxCosts = blueprint.getMaxCosts()
        return runBlueprint(beginState, blueprint, maxCosts, listOf(), 0)
    }

    private fun runBlueprint(state: State, blueprint: Blueprint, maxMaterials: List<Int>, skippedRobots: List<Int>, previousBestGeodes: Int): Int {
//        println("time left: ${state.timeLeft}")
//        println("state: $state")
        if (state.timeLeft == 1) {
            // base case
            return state.materials[3] + state.robots[3]
        }

        if (optimisticBest(state, 3) <= previousBestGeodes) {
            // future cannot beat previous best geode, early exit
            return previousBestGeodes
        }

        if (optimisticBest(state, 2) < maxMaterials[2]) {
            // if we cannot get enough obsidian to create another geode robot, early exit
            return state.materials[3] + state.robots[3] * state.timeLeft
        }

        val newState = State(state.timeLeft - 1, mutableListOf(0, 0, 0, 0), mutableListOf(0, 0, 0, 0))
        (0..3).forEach { i ->
            newState.materials[i] = state.materials[i] + state.robots[i]
            newState.robots[i] = state.robots[i]
        }

        val availableRobots = state.availableRobots(blueprint)
        if (availableRobots.contains(3)) {
//            println("bought geode robot")
            // geode robot available to buy, immediately do it
            newState.buildRobot(blueprint, 3)
            return runBlueprint(newState, blueprint, maxMaterials, listOf(), previousBestGeodes)
        }

        // cannot buy geode robot
        // check what happens when we individually buy each available robot, and what happens when we do nothing
        // but only buy robots that we could not buy previously, because why buy them now when we could buy them last turn

        var best = previousBestGeodes
        availableRobots.filter { it !in skippedRobots }
            .forEach { robotType ->
//                println("try buying robot $robotType")
                newState.buildRobot(blueprint, robotType)
                val score = runBlueprint(newState, blueprint, maxMaterials, listOf(), previousBestGeodes)
                best = max(best, score)
                newState.unbuildRobot(blueprint, robotType)
            }
        // check what happens if we build nothing
        val score = runBlueprint(newState, blueprint, maxMaterials, availableRobots, previousBestGeodes)
        best = max(best, score)

        return best
    }

    private fun optimisticBest(state: State, materialIndex: Int): Int {
        return state.materials[materialIndex] + // current materials
                state.robots[materialIndex] * state.timeLeft +// future materials for current robots
                state.timeLeft * (state.timeLeft - 1) / 2// optimistic points if every future turn a new robot gets created
    }

    data class State(val timeLeft: Int, val robots: MutableList<Int>, val materials: MutableList<Int>) {
        fun availableRobots(blueprint: Blueprint): List<Int> {
            val robots = mutableListOf<Int>()
            if (materials[0] >= blueprint.oreCost) {
                robots.add(0)
            }
            if (materials[0] >= blueprint.clayCost) {
                robots.add(1)
            }
            if (materials[0] >= blueprint.obsidianCost.first && materials[1] >= blueprint.obsidianCost.second) {
                robots.add(2)
            }
            if (materials[0] >= blueprint.geodeCost.first && materials[2] >= blueprint.geodeCost.second) {
                robots.add(3)
            }
            return robots
        }

        fun buildRobot(blueprint: Blueprint, robotType: Int) {
            when (robotType) {
                0 -> materials[0] = materials[0] - blueprint.oreCost
                1 -> materials[0] = materials[0] - blueprint.clayCost
                2 -> {
                    materials[0] = materials[0] - blueprint.obsidianCost.first
                    materials[1] = materials[1] - blueprint.obsidianCost.second
                }

                3 -> {
                    materials[0] = materials[0] - blueprint.geodeCost.first
                    materials[2] = materials[2] - blueprint.geodeCost.second
                }


                else -> throw IllegalArgumentException("do not recognize robot type $robotType")
            }
            robots[robotType] = robots[robotType] + 1
        }

        fun unbuildRobot(blueprint: Blueprint, robotType: Int) {
            when (robotType) {
                0 -> materials[0] = materials[0] + blueprint.oreCost
                1 -> materials[0] = materials[0] + blueprint.clayCost
                2 -> {
                    materials[0] = materials[0] + blueprint.obsidianCost.first
                    materials[1] = materials[1] + blueprint.obsidianCost.second
                }

                3 -> {
                    materials[0] = materials[0] + blueprint.geodeCost.first
                    materials[2] = materials[2] + blueprint.geodeCost.second
                }


                else -> throw IllegalArgumentException("do not recognize robot type $robotType")
            }
            robots[robotType] = robots[robotType] - 1
        }
    }

    data class Blueprint(val id: Int, val oreCost: Int, val clayCost: Int, val obsidianCost: Pair<Int, Int>, val geodeCost: Pair<Int, Int>) {
        fun getMaxCosts(): List<Int> {
            return listOf(
                max(max(max(oreCost, clayCost), obsidianCost.first), geodeCost.first), // max ore cost
                obsidianCost.second, // clay cost
                geodeCost.second, // obsidian cost
                Int.MAX_VALUE // there is no geode cost, but we want this as high as possible
            )
        }

        companion object {
            private val regex = Regex(
                "Blueprint (\\d+): Each ore robot costs (\\d+) ore. " +
                        "Each clay robot costs (\\d+) ore. " +
                        "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
                        "Each geode robot costs (\\d+) ore and (\\d+) obsidian."
            )

            fun parseBlueprint(str: String): Blueprint {
                val match = regex.find(str)!!
                val (id, oreCost, clayCost, obsOre, obsClay, geodeOre, geodeObs) = match.destructured
                return Blueprint(id.toInt(), oreCost.toInt(), clayCost.toInt(), obsOre.toInt() to obsClay.toInt(), geodeOre.toInt() to geodeObs.toInt())
            }
        }
    }
}
