package syh.year2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {

    private val subject = Puzzle4()

    @Test
    fun a() {
        assertEquals("18", subject.doA("test"))
        assertEquals("2718", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("9", subject.doB("test"))
        assertEquals("2046", subject.doB("actual"))
    }
}
