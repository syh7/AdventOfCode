package syh.year2023.day13

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {

    private val subject = Puzzle13()

    @Test
    fun a() {
        assertEquals(405, subject.doA("test"))
        assertEquals(33780, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(400, subject.doB("test"))
        assertEquals(23479, subject.doB("actual"))
    }
}