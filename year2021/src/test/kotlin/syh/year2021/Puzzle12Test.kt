package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {

    private val subject = Puzzle12()

    @Test
    fun a() {
        assertEquals("10", subject.doA("test"))
        assertEquals("4186", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("36", subject.doB("test"))
        assertEquals("92111", subject.doB("actual"))
    }
}