package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {

    private val subject = Puzzle21()

    @Test
    fun a() {
        assertEquals("152", subject.doA("test"))
        assertEquals("168502451381566", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("301", subject.doB("test"))
        assertEquals("3343167719435", subject.doB("actual"))
    }
}