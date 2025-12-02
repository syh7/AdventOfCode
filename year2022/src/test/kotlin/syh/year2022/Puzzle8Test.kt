package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {

    private val subject = Puzzle8()

    @Test
    fun a() {
        assertEquals("21", subject.doA("test"))
        assertEquals("1845", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("8", subject.doB("test"))
        assertEquals("230112", subject.doB("actual"))
    }
}