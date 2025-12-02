package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {

    private val subject = Puzzle12()

    @Test
    fun a() {
        assertEquals("31", subject.doA("test"))
        assertEquals("361", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("29", subject.doB("test"))
        assertEquals("354", subject.doB("actual"))
    }

}