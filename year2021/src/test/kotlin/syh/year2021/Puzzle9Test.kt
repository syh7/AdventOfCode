package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {

    private val subject = Puzzle9()

    @Test
    fun a() {
        assertEquals("15", subject.doA("test"))
        assertEquals("588", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1134", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}