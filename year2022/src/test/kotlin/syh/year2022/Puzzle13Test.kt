package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {

    private val subject = Puzzle13()

    @Test
    fun a() {
        assertEquals("13", subject.doA("test"))
        assertEquals("6101", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("140", subject.doB("test"))
        assertEquals("21909", subject.doB("actual"))
    }
}