package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {

    private val subject = Puzzle9()

    @Test
    fun a() {
        assertEquals("50", subject.doA("test"))
        assertEquals("4750297200", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("24", subject.doB("test"))
        assertEquals("1578115935", subject.doB("actual"))
    }
}
