package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {

    private val subject = Puzzle18()

    @Test
    fun a() {
        assertEquals("62", subject.doA("test"))
        assertEquals("46334", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("952408144115", subject.doB("test"))
        assertEquals("102000662718092", subject.doB("actual"))
    }
}