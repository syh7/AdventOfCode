package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle22Test {

    private val subject = Puzzle22()

    @Test
    fun a() {
        assertEquals("6032", subject.doA("test"))
        assertEquals("80392", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("5031", subject.doB("test"))
        assertEquals("19534", subject.doB("actual"))
    }
}