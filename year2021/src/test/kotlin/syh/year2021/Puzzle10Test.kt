package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {

    private val subject = Puzzle10()

    @Test
    fun a() {
        assertEquals("26397", subject.doA("test"))
        assertEquals("216297", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("288957", subject.doB("test"))
        assertEquals("2165057169", subject.doB("actual"))
    }
}