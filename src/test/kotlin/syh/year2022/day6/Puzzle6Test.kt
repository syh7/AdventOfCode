package syh.year2022.day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {
    private val subject = Puzzle6()

    @Test
    fun a() {
        assertEquals("5", subject.doA("test"))
        assertEquals("1766", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("23", subject.doB("test"))
        assertEquals("2383", subject.doB("actual"))
    }
}