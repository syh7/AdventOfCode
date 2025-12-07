package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {

    private val subject = Puzzle17()

    @Test
    fun a() {
        assertEquals("102", subject.doA("test"))
        assertEquals("1065", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("94", subject.doB("test"))
        assertEquals("1249", subject.doB("actual"))
    }
}