package syh.year2023.day7

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {

    private val subject = Puzzle7()

    @Test
    fun a() {
        assertEquals(6440, subject.doA("test"))
        assertEquals(250898830, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(5905, subject.doB("test"))
        assertEquals(252127335, subject.doB("actual"))
    }
}