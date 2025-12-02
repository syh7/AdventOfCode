package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun a() {
        assertEquals("4361", subject.doA("test"))
        assertEquals("522726", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("467835", subject.doB("test"))
        assertEquals("81721933", subject.doB("actual"))
    }
}