package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {
    private val subject = Puzzle15()

    @Test
    fun a() {
        assertEquals("2028", subject.doA("test1"))
        assertEquals("10092", subject.doA("test2"))
        assertEquals("1349898", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("618", subject.doB("test3"))
        assertEquals("9021", subject.doB("test2"))
        assertEquals("1376686", subject.doB("actual"))
    }
}