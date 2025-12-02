package syh.library

import java.util.Stack

data class Node<V>(
    val value: V,
    val neighbours: MutableMap<Node<V>, Long> = mutableMapOf(),
    val predecessors: MutableList<Node<V>> = mutableListOf(),
    var distance: Long = Long.MAX_VALUE
) : Comparable<Node<V>> {

    fun reset() {
        this.predecessors.clear()
        this.distance = Long.MAX_VALUE
    }

    fun getAllPredecessors(): Set<Node<V>> {
        val totalPredecessors = mutableSetOf<Node<V>>()

        val stack = Stack<Node<V>>()
        for (predecessor in predecessors) stack.push(predecessor)
        while (stack.isNotEmpty()) {
            val pop = stack.pop()
            if (pop !in totalPredecessors) {
                totalPredecessors.add(pop)
                pop.predecessors.forEach { stack.push(it) }
            }
        }
        return totalPredecessors
    }

    override fun compareTo(other: Node<V>): Int {
        return this.distance.compareTo(other.distance)
    }

    override fun toString(): String {
        return "Node($value, $distance)"
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != this.javaClass) return false
        return this.value?.equals((other as Node<V>).value) ?: false
    }

}