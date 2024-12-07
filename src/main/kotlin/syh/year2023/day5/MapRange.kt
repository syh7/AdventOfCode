package syh.year2023.day5

data class MapRange(val destination: Long, val source: Long, val range: Long) {
    fun contains(value: Long): Boolean {
        return value in source until source + range
    }

    fun calculate(value: Long): Long {
        return value + destination - source
    }
}