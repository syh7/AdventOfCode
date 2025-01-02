package syh.year2022.day18

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {

    private val subject = Puzzle18()

    @Test
    fun a() {
        assertEquals("64", subject.doA("test"))
        assertEquals("3610", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("58", subject.doB("test"))
        assertEquals("2082", subject.doB("actual"))
    }
}