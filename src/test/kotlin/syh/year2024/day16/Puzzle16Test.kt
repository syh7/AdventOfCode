package syh.year2024.day16


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    private val subject = Puzzle16()

    @Test
    fun a() {
        assertEquals("7036", subject.doA("test1"))
        assertEquals("11048", subject.doA("test2"))
        assertEquals("4013", subject.doA("test3"))
        assertEquals("7036", subject.doA("test4"))
        assertEquals("85480", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("45", subject.doB("test1"))
        assertEquals("64", subject.doB("test2"))
        assertEquals("53", subject.doB("test4"))
        assertEquals("518", subject.doB("actual"))
    }
}