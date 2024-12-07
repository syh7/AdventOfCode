package syh.year2024.day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun a() {
        assertEquals(2, subject.doA("test"))
        assertEquals(432, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(4, subject.doB("test"))
        assertEquals(488, subject.doB("actual"))
    }
}
