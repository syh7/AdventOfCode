package syh.year2024.day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {
    private val subject = Puzzle6()

    @Test
    fun a() {
        assertEquals(41, subject.doA("test"))
        assertEquals(5162, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(6, subject.doB("test"))
        assertEquals(1909, subject.doB("actual"))
    }
}