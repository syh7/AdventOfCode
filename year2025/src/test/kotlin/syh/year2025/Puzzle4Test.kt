package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun a() {
        assertEquals("13", subject.doA("test"))
        assertEquals("1349", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("43", subject.doB("test"))
        assertEquals("8277", subject.doB("actual"))
    }
}
