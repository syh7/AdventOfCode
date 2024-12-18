package syh.year2024.day18


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {
    private val subject = Puzzle18()

    @Test
    fun a() {
        assertEquals("22", subject.doA("test"))
        assertEquals("314", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("6,1", subject.doB("test"))
        assertEquals("15,20", subject.doB("actual"))
    }
}