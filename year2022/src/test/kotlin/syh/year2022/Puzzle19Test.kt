package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {

    private val subject = Puzzle19()

    @Test
    fun a() {
        assertEquals("33", subject.doA("test"))
        assertEquals("1981", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3472", subject.doB("test"))
        assertEquals("10962", subject.doB("actual"))
    }
}