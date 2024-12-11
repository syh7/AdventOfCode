package syh.year2023.day19

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {

    private val subject = Puzzle19()

    @Test
    fun a() {
        assertEquals("19114", subject.doA("test"))
        assertEquals("353553", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("167409079868000", subject.doB("test"))
        assertEquals("124615747767410", subject.doB("actual"))
    }
}