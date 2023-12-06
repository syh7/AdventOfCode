package year2023.day5

data class SeedRange(val start: Long, val range: Long) {
    fun contains(value: Long): Boolean {
        return value in start until start + range
    }
}