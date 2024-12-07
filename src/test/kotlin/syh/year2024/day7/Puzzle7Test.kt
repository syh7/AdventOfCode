package syh.year2024.day7


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {
    private val subject = Puzzle7()

    @Test
    fun a() {
        assertEquals(3749, subject.doA("test"))
        assertEquals(932137732557, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(11387, subject.doB("test"))
        assertEquals(661823605105500, subject.doB("actual"))
    }
}