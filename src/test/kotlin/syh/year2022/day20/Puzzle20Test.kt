package syh.year2022.day20

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle20Test {

    private val subject = Puzzle20()

    @Test
    fun a() {
        assertEquals("3", subject.doA("test"))
        assertEquals("8721", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1623178306", subject.doB("test"))
        assertEquals("831878881825", subject.doB("actual"))
    }
}