package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {

    private val subject = Puzzle11()

    @Test
    fun a() {
        assertEquals("374", subject.doA("test"))
        assertEquals("9563821", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("82000210", subject.doB("test"))
        assertEquals("827009909817", subject.doB("actual"))
    }
}