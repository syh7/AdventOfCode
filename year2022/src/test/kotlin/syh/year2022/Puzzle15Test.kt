package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {

    private val subject = Puzzle15()

    @Test
    fun a() {
        assertEquals("26", subject.doA("test"))
        assertEquals("4907780", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("56000011", subject.doB("test"))
        assertEquals("13639962836448", subject.doB("actual"))
    }
}