package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun a() {
        assertEquals("357", subject.doA("test"))
        assertEquals("17316", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3121910778619", subject.doB("test"))
        assertEquals("171741365473332", subject.doB("actual"))
    }
}
