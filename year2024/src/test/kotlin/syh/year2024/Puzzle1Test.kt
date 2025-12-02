package syh.year2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun a() {
        assertEquals("11", subject.doA("test"))
        assertEquals("2430334", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("31", subject.doB("test"))
        assertEquals("28786472", subject.doB("actual"))
    }
}
