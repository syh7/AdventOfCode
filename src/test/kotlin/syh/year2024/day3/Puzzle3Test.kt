package syh.year2024.day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun a() {
        assertEquals("161", subject.doA("test"))
        assertEquals("170778545", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("48", subject.doB("test"))
        assertEquals("82868252", subject.doB("actual"))
    }
}
