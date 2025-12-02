package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {

    private val subject = Puzzle7()

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
