package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle22Test {

    private val subject = Puzzle22()

    @Test
    fun a() {
        assertEquals("5", subject.doA("test"))
        assertEquals("430", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("7", subject.doB("test"))
        assertEquals("60558", subject.doB("actual"))
    }
}