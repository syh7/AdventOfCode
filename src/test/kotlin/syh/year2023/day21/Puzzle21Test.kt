package syh.year2023.day21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import syh.year2023.day5.Puzzle5

class Puzzle21Test {

    private val subject = Puzzle5()

    @Test
    fun a() {
        assertEquals(35, subject.doA("test"))
        assertEquals(57075758, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(46, subject.doB("test"))
        assertEquals(31161857, subject.doB("actual"))
    }
}