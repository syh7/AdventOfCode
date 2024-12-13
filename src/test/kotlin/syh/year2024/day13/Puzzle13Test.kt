package syh.year2024.day13


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {
    private val subject = Puzzle13()

    @Test
    fun a() {
        assertEquals("480", subject.doA("test"))
        assertEquals("29598", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("875318608908", subject.doB("test"))
        assertEquals("93217456941970", subject.doB("actual"))
    }
}