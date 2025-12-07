package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {

    private val subject = Puzzle14()

    @Test
    fun a() {
        assertEquals("1588", subject.doA("test"))
        assertEquals("2435", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2188189693529", subject.doB("test"))
        assertEquals("2587447599164", subject.doB("actual"))
    }
}