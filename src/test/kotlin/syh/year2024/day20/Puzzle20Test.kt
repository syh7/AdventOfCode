package syh.year2024.day20


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle20Test {
    private val subject = Puzzle20()

    @Test
    fun a() {
        assertEquals("5", subject.doA("test"))
        assertEquals("1402", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1449", subject.doB("test"))
        assertEquals("1020244", subject.doB("actual"))
    }
}