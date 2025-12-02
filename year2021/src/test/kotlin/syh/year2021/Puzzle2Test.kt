package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun a() {
        assertEquals("150", subject.doA("test"))
        assertEquals("1648020", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("900", subject.doB("test"))
        assertEquals("1759818555", subject.doB("actual"))
    }
}