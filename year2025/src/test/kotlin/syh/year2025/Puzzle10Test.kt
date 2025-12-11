package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {

    private val subject = Puzzle10()

    @Test
    fun a() {
        assertEquals("7", subject.doA("test"))
        assertEquals("488", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("33", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}
