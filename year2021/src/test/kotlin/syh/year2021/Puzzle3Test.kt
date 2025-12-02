package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun a() {
        assertEquals("198", subject.doA("test"))
        assertEquals("4006064", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("230", subject.doB("test"))
        assertEquals("5941884", subject.doB("actual"))
    }
}