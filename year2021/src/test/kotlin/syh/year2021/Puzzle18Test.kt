package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {

    private val subject = Puzzle18()

    @Test
    fun a() {
        assertEquals("4140", subject.doA("test"))
        assertEquals("4243", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3993", subject.doB("test"))
        assertEquals("4701", subject.doB("actual"))
    }
}