package syh.year2024.day25


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle25Test {
    private val subject = Puzzle25()

    @Test
    fun a() {
        assertEquals("3", subject.doA("test"))
        assertEquals("3338", subject.doA("actual"))
    }
}