package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun a() {
        assertEquals("3", subject.doA("test"))
        assertEquals("1084", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("6", subject.doB("test"))
        assertEquals("6475", subject.doB("actual"))
    }
}
