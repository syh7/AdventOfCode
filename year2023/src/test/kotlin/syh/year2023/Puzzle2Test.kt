package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun a() {
        assertEquals("8", subject.doA("test"))
        assertEquals("2727", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2286", subject.doB("test"))
        assertEquals("56580", subject.doB("actual"))
    }
}