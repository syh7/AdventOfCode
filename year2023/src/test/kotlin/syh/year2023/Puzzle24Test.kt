package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {

    private val subject = Puzzle24()

    @Test
    fun a() {
        assertEquals("2", subject.doA("test"))
        assertEquals("16018", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("47", subject.doB("test"))
        assertEquals("1004774995964534", subject.doB("actual"))
    }
}