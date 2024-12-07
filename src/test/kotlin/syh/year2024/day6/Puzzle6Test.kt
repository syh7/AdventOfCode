package syh.year2024.day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {
    private val subject = Puzzle6()

    @Test
    fun test() {
        assertEquals(41, subject.doA("test"))
        assertEquals(6, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(5162, subject.doA("actual"))
        assertEquals(1909, subject.doB("actual"))
    }
}