package syh.year2024.day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {
    private val subject = Puzzle5()

    @Test
    fun test() {
        assertEquals(143, subject.doA("test"))
        assertEquals(123, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(5087, subject.doA("actual"))
        assertEquals(4971, subject.doB("actual"))
    }
}