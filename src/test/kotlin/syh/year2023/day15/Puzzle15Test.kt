package syh.year2023.day15

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {

    private val subject = Puzzle15()

    @Test
    fun a() {
        assertEquals(1320, subject.doA("test"))
        assertEquals(520500, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(145, subject.doB("test"))
        assertEquals(213097, subject.doB("actual"))
    }
}