package syh.year2022.day2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun a() {
        assertEquals("15", subject.doA("test"))
        assertEquals("11841", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("12", subject.doB("test"))
        assertEquals("13022", subject.doB("actual"))
    }
}