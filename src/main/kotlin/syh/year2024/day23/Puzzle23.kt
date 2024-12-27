package syh.year2024.day23

import syh.AbstractAocDay


class Puzzle23 : AbstractAocDay(2024, 23) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
        val connections = lines.map { it.split("-") }

//        val graph = Graph<String>()

        val computerMap = connections.flatten().toSet().map { Computer(it) }.associateBy { it.id }
        for ((a, b) in connections) {
            computerMap[a]!!.neighbours.add(computerMap[b]!!)
            computerMap[b]!!.neighbours.add(computerMap[a]!!)
//            graph.addEdge(a, b, 1)
//            graph.addEdge(b, a, 1)

        }

        val computers = computerMap.values.toList()
//        computers.forEach { println(it) }

//        graph.writePlantUml(getFileNameToWrite(file, ""))

        val otherCliques = mutableSetOf<Set<Computer>>()
        computers.map { pc1 ->
            pc1.neighbours.forEach { pc2 ->
                pc2.neighbours.filter { pc3 -> pc3 in pc1.neighbours }
                    .forEach { pc3 -> otherCliques.add(setOf(pc1, pc2, pc3)) }
            }
        }

//        println("other cliques ${otherCliques.size}")
//        otherCliques.forEach { println(it.map { c -> c.id }) }

        val validCliques = otherCliques
            .filter { it.size == 3 }
            .filter { it.any { c -> c.id.startsWith("t") } }
            .sortedBy { list -> list.minOf { it.id } }

        println("found ${validCliques.size} valid cliques")
//        validCliques.forEach { println(it.map { c -> c.id }.sorted()) }

        return validCliques.size.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        val connections = lines.map { it.split("-") }

        val computerMap = connections.flatten().toSet().map { Computer(it) }.associateBy { it.id }
        for ((a, b) in connections) {
            computerMap[a]!!.neighbours.add(computerMap[b]!!)
            computerMap[b]!!.neighbours.add(computerMap[a]!!)

        }

        val computers = computerMap.values.toList()
//        computers.forEach { println(it) }

        val cliques = mutableSetOf<Set<Computer>>()
        findCliquesWithBronKerbosch(cliques, computers.toMutableSet(), mutableSetOf(), mutableSetOf())

//        println("found ${cliques.size} cliques:")
//        cliques.forEach { println(it) }

        val maxClique = cliques.maxBy { it.size }.sortedBy { it.id }
        println("found maxClique of size ${maxClique.size}: ${maxClique.map { it.id }}")
        return maxClique.joinToString(",") { it.id }
    }

    private fun findCliquesWithBronKerbosch(foundCliques: MutableSet<Set<Computer>>, candidates: MutableSet<Computer>, currentPossibleClique: MutableSet<Computer>, found: MutableSet<Computer>) {
        val candidateCopy = mutableSetOf<Computer>()
        candidateCopy.addAll(candidates)

        if (end(candidates, found)) {
            return
        }

        for (candidate in candidateCopy) {
            val newCandidates = mutableSetOf<Computer>()
            val newFound = mutableSetOf<Computer>()

            currentPossibleClique.add(candidate)
            candidates.remove(candidate)

            for (possibleNewCandidate in candidates) {
                if (possibleNewCandidate in candidate.neighbours) {
                    newCandidates.add(possibleNewCandidate)
                }
            }

            for (possibleNewFound in found) {
                if (possibleNewFound in candidate.neighbours) {
                    newFound.add(possibleNewFound)
                }
            }

            if (newCandidates.isEmpty() && newFound.isEmpty()) {
                val cliqueCopy = mutableSetOf<Computer>()
                cliqueCopy.addAll(currentPossibleClique)
                foundCliques.add(cliqueCopy)
            } else {
                findCliquesWithBronKerbosch(foundCliques, newCandidates, currentPossibleClique, newFound)
            }

            found.add(candidate)
            currentPossibleClique.remove(candidate)

        }
    }

    private fun end(candidates: Set<Computer>, alreadyFound: Set<Computer>): Boolean {
        // if a node in already_found is connected to all nodes in candidates
        var edgecounter: Int

        for (found in alreadyFound) {
            edgecounter = 0
            for (candidate in candidates) {
                if (found in candidate.neighbours) {
                    edgecounter++
                }
            }

            if (edgecounter == candidates.size) {
                return true
            }
        }

        return false
    }

    data class Computer(val id: String, val neighbours: MutableSet<Computer> = mutableSetOf()) : Comparable<Computer> {
        override fun compareTo(other: Computer): Int {
            return this.id.compareTo(other.id)
        }

        override fun toString(): String {
            return "Computer(id=$id, neighbours=${neighbours.joinToString { it.id }})"
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return this.hashCode() == other.hashCode()
        }
    }
}
