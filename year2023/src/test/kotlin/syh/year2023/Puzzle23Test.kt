package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle23Test {

    private val subject = Puzzle23()

    @Test
    fun a() {
        assertEquals("94", subject.doA("test"))
        assertEquals("2114", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("154", subject.doB("test"))
        assertEquals("6322", subject.doB("actual"))
    }
}