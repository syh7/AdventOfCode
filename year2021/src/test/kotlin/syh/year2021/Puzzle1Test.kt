package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun a() {
        assertEquals("7", subject.doA("test"))
        assertEquals("1583", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("5", subject.doB("test"))
        assertEquals("1627", subject.doB("actual"))
    }
}