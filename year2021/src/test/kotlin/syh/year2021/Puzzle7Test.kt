package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {

    private val subject = Puzzle7()

    @Test
    fun a() {
        assertEquals("37", subject.doA("test"))
        assertEquals("355521", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("168", subject.doB("test"))
        assertEquals("100148777", subject.doB("actual"))
    }
}