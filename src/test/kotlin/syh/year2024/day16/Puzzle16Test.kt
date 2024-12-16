package syh.year2024.day16


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    private val subject = Puzzle16()

    @Test
    fun a() {
        assertEquals("7036", subject.doA("test1"))
        assertEquals("11048", subject.doA("test2"))
        assertEquals("", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("", subject.doB("test2"))
        assertEquals("", subject.doB("actual"))
    }
}