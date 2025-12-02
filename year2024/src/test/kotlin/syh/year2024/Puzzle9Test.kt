package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {
    private val subject = Puzzle9()

    @Test
    fun a() {
        assertEquals("1928", subject.doA("test"))
        assertEquals("6337367222422", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2858", subject.doB("test"))
        assertEquals("6361380647183", subject.doB("actual"))
    }
}