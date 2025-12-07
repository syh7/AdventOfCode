package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {

    private val subject = Puzzle11()

    @Test
    fun a() {
        assertEquals("1656", subject.doA("test"))
        assertEquals("1655", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("195", subject.doB("test"))
        assertEquals("337", subject.doB("actual"))
    }
}