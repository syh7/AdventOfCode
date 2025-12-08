package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {

    private val subject = Puzzle8()

    @Test
    fun a() {
        assertEquals("40", subject.doA("test"))
        assertEquals("72150", subject.doA("actual")) // 5760
    }

    @Test
    fun b() {
        assertEquals("25272", subject.doB("test"))
        assertEquals("3926518899", subject.doB("actual"))
    }
}
