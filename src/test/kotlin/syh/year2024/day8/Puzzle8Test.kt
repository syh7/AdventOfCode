package syh.year2024.day8


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {
    private val subject = Puzzle8()

    @Test
    fun a() {
        assertEquals("14", subject.doA("test"))
        assertEquals("423", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("34", subject.doB("test"))
        assertEquals("1287", subject.doB("actual"))
    }
}