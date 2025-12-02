package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun a() {
        assertEquals("2", subject.doA("test"))
        assertEquals("456", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("4", subject.doB("test"))
        assertEquals("808", subject.doB("actual"))
    }
}