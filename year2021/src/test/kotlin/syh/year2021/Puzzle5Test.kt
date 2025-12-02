package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {

    private val subject = Puzzle5()

    @Test
    fun a() {
        assertEquals("5", subject.doA("test"))
        assertEquals("6564", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("12", subject.doB("test"))
        assertEquals("19172", subject.doB("actual"))
    }
}