package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {

    private val subject = Puzzle17()

    @Test
    fun a() {
        assertEquals("", subject.doA("test"))
        assertEquals("", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}