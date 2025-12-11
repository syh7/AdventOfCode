package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {

    private val subject = Puzzle11()

    @Test
    fun a() {
        assertEquals("5", subject.doA("test"))
        assertEquals("585", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2", subject.doB("testB"))
        assertEquals("", subject.doB("actual"))
    }
}
