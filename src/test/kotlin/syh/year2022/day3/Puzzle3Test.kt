package syh.year2022.day3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {

    private val subject = Puzzle3()

    @Test
    fun a() {
        assertEquals("157", subject.doA("test"))
        assertEquals("8240", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("70", subject.doB("test"))
        assertEquals("2587", subject.doB("actual"))
    }
}