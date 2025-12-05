package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {

    private val subject = Puzzle8()

    @Test
    fun a() {
        assertEquals("26", subject.doA("test"))
        assertEquals("381", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("61229", subject.doB("test"))
        assertEquals("1023686", subject.doB("actual"))
    }
}