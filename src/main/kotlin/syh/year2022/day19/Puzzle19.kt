package syh.year2022.day19

import syh.AbstractAocDay


class Puzzle19 : AbstractAocDay(2022, 19) {

    override fun doA(file: String): String {
        val blueprints = readSingleLineFile(file).map { Blueprint.parseBlueprint(it) }
        val sum = blueprints
            .sumOf {
                println("starting blueprint ${it.id}")
                val score = runBlueprint(it, 24)
                println("blueprint ${it.id} had score $score")
                it.id * score
            }
        return sum.toString()
    }

    override fun doB(file: String): String {
        val blueprints = readSingleLineFile(file)
            .take(3) // only first 3 matter
            .map { Blueprint.parseBlueprint(it) }
        val sum = blueprints
            .map {
                println("starting blueprint ${it.id}")
                val score = runBlueprint(it, 32)
                println("blueprint ${it.id} had score $score")
                score
            }
            .reduce { t, u -> t * u }
        return sum.toString()
    }

    private fun runBlueprint(blueprint: Blueprint, timeLeft: Int): Int {
        // create robot list of cost to material
        val robotCosts = listOf(
            listOf(0, 0, 0, blueprint.oreCost) to listOf(0, 0, 0, 1),
            listOf(0, 0, 0, blueprint.clayCost) to listOf(0, 0, 1, 0),
            listOf(0, 0, blueprint.obsidianCost.second, blueprint.obsidianCost.first) to listOf(0, 1, 0, 0),
            listOf(0, blueprint.geodeCost.second, 0, blueprint.geodeCost.first) to listOf(1, 0, 0, 0),
            listOf(0, 0, 0, 0) to listOf(0, 0, 0, 0),
        )
        
        // initially start with no material and 1 ore robot
        var work = listOf(Triple(listOf(0, 0, 0, 0), listOf(0, 0, 0, 1), listOf(0, 0, 0, 1)))

        for (t in 0..<timeLeft) {
            val newWork = mutableSetOf<Triple<List<Int>, List<Int>, List<Int>>>()

            for ((have, make, _) in work) { // for this value list
                for ((cost, more) in robotCosts) { // check each robot

                    var canBuy = true
                    for (i in cost.indices) {
                        if (cost[i] > have[i]) canBuy = false
                    }

                    if (canBuy) { // if we can buy this robot, do so, and add the new state to work
                        val newMake = mutableListOf(0, 0, 0, 0)
                        val newHave = mutableListOf(0, 0, 0, 0)
                        val newKey = mutableListOf(0, 0, 0, 0)
                        for (i in cost.indices) {
                            newHave[i] = have[i] + make[i] - cost[i]
                            newMake[i] = make[i] + more[i]
                            newKey[i] = newHave[i] + newMake[i]
                        }
                        newWork.add(Triple(newHave, newMake, newKey))
                    }
                }
            }
            // prune the new work, only take the first 750 states
            work = newWork
                .sortedWith(compareBy<Triple<List<Int>, List<Int>, List<Int>>>({ it.third[0] }, { it.third[1] }, { it.third[2] }, { it.third[3] }).reversed())
                .take(750)
        }
        return work.map { it.first }.maxOf { it[0] }
    }

    data class Blueprint(val id: Int, val oreCost: Int, val clayCost: Int, val obsidianCost: Pair<Int, Int>, val geodeCost: Pair<Int, Int>) {

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
