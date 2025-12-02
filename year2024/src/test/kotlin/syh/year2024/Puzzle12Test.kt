package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {
    private val subject = Puzzle12()

    @Test
    fun a() {
        assertEquals("140", subject.doA("test1"))
        assertEquals("772", subject.doA("test2"))
        assertEquals("1930", subject.doA("test3"))
        assertEquals("1486324", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("80", subject.doB("test1"))
        assertEquals("436", subject.doB("test2"))
        assertEquals("236", subject.doB("test4"))
        assertEquals("368", subject.doB("test5"))
        assertEquals("1206", subject.doB("test3"))
        assertEquals("898684", subject.doB("actual"))
    }
}