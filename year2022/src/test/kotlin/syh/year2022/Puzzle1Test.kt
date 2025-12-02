package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun a() {
        assertEquals("24000", subject.doA("test"))
        assertEquals("68292", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("45000", subject.doB("test"))
        assertEquals("203203", subject.doB("actual"))
    }
}