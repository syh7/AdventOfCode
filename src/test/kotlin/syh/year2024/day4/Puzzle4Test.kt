package syh.year2024.day4

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun test() {
        assertEquals(18, subject.doA("test"))
        assertEquals(9, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(2718, subject.doA("actual"))
        assertEquals(2046, subject.doB("actual"))
    }
}
