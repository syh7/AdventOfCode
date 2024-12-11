package syh.year2024.day11


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {
    private val subject = Puzzle11()

    @Test
    fun a() {
        assertEquals(55312, subject.doA("test"))
        assertEquals(194557, subject.doA("actual"))
    }

    @Test
    fun b() {
//        assertEquals(0, subject.doB("test"))
        assertEquals(0, subject.doB("actual"))
    }
}