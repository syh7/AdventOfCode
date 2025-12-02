package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {

    private val subject = Puzzle14()

    @Test
    fun a() {
        assertEquals("136", subject.doA("test"))
        assertEquals("110565", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("64", subject.doB("test"))
        assertEquals("89845", subject.doB("actual"))
    }
}