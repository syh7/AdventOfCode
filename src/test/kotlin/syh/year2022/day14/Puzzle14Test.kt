package syh.year2022.day14

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {

    private val subject = Puzzle14()

    @Test
    fun a() {
        assertEquals("24", subject.doA("test"))
        assertEquals("757", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("93", subject.doB("test"))
        assertEquals("24943", subject.doB("actual"))
    }
}