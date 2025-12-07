package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {

    private val subject = Puzzle15()

    @Test
    fun a() {
        assertEquals("40", subject.doA("test"))
        assertEquals("498", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("315", subject.doB("test"))
        assertEquals("2901", subject.doB("actual"))
    }
}