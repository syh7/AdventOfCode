package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {

    private val subject = Puzzle17()

    @Test
    fun a() {
        assertEquals("45", subject.doA("test"))
        assertEquals("3916", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("112", subject.doB("test"))
        assertEquals("2986", subject.doB("actual"))
    }
}