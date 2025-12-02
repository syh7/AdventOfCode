package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun a() {
        assertEquals("13", subject.doA("test"))
        assertEquals("21821", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("30", subject.doB("test"))
        assertEquals("5539496", subject.doB("actual"))
    }
}