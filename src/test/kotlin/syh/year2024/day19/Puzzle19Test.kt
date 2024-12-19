package syh.year2024.day19


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    private val subject = Puzzle19()

    @Test
    fun a() {
        assertEquals("6", subject.doA("test"))
        assertEquals("365", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("16", subject.doB("test"))
        assertEquals("730121486795169", subject.doB("actual"))
    }
}