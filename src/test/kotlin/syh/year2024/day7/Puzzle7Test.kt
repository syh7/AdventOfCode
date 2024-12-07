package syh.year2024.day7


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {
    private val subject = Puzzle7()

    @Test
    fun test() {
        assertEquals(3749, subject.doA("test"))
        assertEquals(11387, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(932137732557, subject.doA("actual"))
        assertEquals(661823605105500, subject.doB("actual"))
    }
}