package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {

    private val subject = Puzzle16()

    @Test
    fun a() {
        assertEquals("1651", subject.doA("test"))
        assertEquals("1720", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1707", subject.doB("test"))
        assertEquals("2582", subject.doB("actual"))
    }
}