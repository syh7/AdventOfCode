package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {

    private val subject = Puzzle5()

    @Test
    fun a() {
        assertEquals("3", subject.doA("test"))
        assertEquals("613", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("14", subject.doB("test"))
        assertEquals("336495597913098", subject.doB("actual"))
    }
}
