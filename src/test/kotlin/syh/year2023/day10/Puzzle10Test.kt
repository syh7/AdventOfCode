package syh.year2023.day10

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {

    private val subject = Puzzle10()

    @Test
    fun a() {
        assertEquals("8", subject.doA("testA"))
        assertEquals("6890", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("10", subject.doB("testB"))
        assertEquals("453", subject.doB("actual"))
    }
}