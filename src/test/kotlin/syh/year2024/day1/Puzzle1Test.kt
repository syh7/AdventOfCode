package syh.year2024.day1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun test() {
        assertEquals(11, subject.doA("test"))
        assertEquals(31, subject.doB("test"))
    }

    @Test
    fun actual() {
        assertEquals(2430334, subject.doA("actual"))
        assertEquals(28786472, subject.doB("actual"))
    }
}
