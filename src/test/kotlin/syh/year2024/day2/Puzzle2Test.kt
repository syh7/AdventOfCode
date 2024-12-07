package syh.year2024.day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun test() {
        assertEquals(2, subject.doA("test"))
        assertEquals(4, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(432, subject.doA("actual"))
        assertEquals(488, subject.doB("actual"))
    }
}
