package syh.year2022.day9

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {

    private val subject = Puzzle9()

    @Test
    fun a() {
        assertEquals("13", subject.doA("testA"))
        assertEquals("6067", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("36", subject.doB("testB"))
        assertEquals("2471", subject.doB("actual"))
    }
}