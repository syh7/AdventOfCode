package syh.year2024.day14


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {
    private val subject = Puzzle14()

    @Test
    fun a() {
        assertEquals("12", subject.doA("test"))
        assertEquals("215987200", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1", subject.doB("test"))
        assertEquals("8050", subject.doB("actual"))
    }
}