package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {
    private val subject = Puzzle21()

    @Test
    fun a() {
        assertEquals("126384", subject.doA("test"))
        assertEquals("231564", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("154115708116294", subject.doB("test"))
        assertEquals("281212077733592", subject.doB("actual"))
    }
}