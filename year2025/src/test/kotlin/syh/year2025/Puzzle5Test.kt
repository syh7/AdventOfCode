package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {

    private val subject = Puzzle5()

    @Test
    fun a() {
        assertEquals("", subject.doA("test"))
        assertEquals("", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}
