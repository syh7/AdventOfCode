package syh.year2022.day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {

    private val subject = Puzzle11()

    @Test
    fun a() {
        assertEquals("10605", subject.doA("test"))
        assertEquals("51075", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("2713310158", subject.doB("test"))
        assertEquals("11741456163", subject.doB("actual"))
    }

}