package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {

    private val subject = Puzzle24()

    @Test
    fun a() {
        assertEquals("18", subject.doA("test"))
        assertEquals("332", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("54", subject.doB("test"))
        assertEquals("942", subject.doB("actual"))
    }
}