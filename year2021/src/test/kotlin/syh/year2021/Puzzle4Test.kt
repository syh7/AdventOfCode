package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun a() {
        assertEquals("4512", subject.doA("test"))
        assertEquals("39984", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1924", subject.doB("test"))
        assertEquals("8468", subject.doB("actual"))
    }
}