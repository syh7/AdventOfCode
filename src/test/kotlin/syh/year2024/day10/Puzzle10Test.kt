package syh.year2024.day10


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {
    private val subject = Puzzle10()

    @Test
    fun a() {
        assertEquals("2", subject.doA("test1"))
        assertEquals("4", subject.doA("test2"))
        assertEquals("3", subject.doA("test3"))
        assertEquals("36", subject.doA("test4"))
        assertEquals("760", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3", subject.doB("test5"))
        assertEquals("13", subject.doB("test6"))
        assertEquals("227", subject.doB("test7"))
        assertEquals("81", subject.doB("test4"))
        assertEquals("1764", subject.doB("actual"))
    }
}