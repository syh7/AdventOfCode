package syh.year2023.day6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {

    private val subject = Puzzle6()

    @Test
    fun a() {
        assertEquals("288", subject.doA("test"))
        assertEquals("781200", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("71503", subject.doB("test"))
        assertEquals("49240091", subject.doB("actual"))
    }
}