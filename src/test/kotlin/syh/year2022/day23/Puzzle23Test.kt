package syh.year2022.day23

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle23Test {

    private val subject = Puzzle23()

    @Test
    fun a() {
        assertEquals("25", subject.doA("test1"))
        assertEquals("110", subject.doA("test2"))
        assertEquals("4005", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("4", subject.doB("test1"))
        assertEquals("20", subject.doB("test2"))
        assertEquals("1008", subject.doB("actual"))
    }
}