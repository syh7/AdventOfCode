package syh.year2023.day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {

    private val subject = Puzzle9()

    @Test
    fun a() {
        assertEquals("114", subject.doA("test"))
        assertEquals("1581679977", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2", subject.doB("test"))
        assertEquals("889", subject.doB("actual"))
    }
}