package syh.year2024.day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun test() {
        assertEquals(161, subject.doA("test"))
        assertEquals(48, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(170778545, subject.doA("actual"))
        assertEquals(82868252, subject.doB("actual"))
    }
}
