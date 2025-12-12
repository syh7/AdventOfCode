package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {

    private val subject = Puzzle12()

    @Test
    fun a() {
        assertEquals("2", subject.doA("test"))
        assertEquals("443", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}
