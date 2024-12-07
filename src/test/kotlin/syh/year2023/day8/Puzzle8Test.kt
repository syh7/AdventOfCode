package syh.year2023.day8

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {

    private val subject = Puzzle8()

    @Test
    fun a() {
        assertEquals(2, subject.doA("testA1"))
        assertEquals(6, subject.doA("testA2"))
        assertEquals(20093, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(6, subject.doB("testB"))
        assertEquals(22103062509257, subject.doB("actual"))
    }
}