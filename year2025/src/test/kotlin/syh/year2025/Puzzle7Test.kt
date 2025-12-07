package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {

    private val subject = Puzzle7()

    @Test
    fun a() {
        assertEquals("21", subject.doA("test"))
        assertEquals("1678", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("40", subject.doB("test"))
        assertEquals("357525737893560", subject.doB("actual"))
    }
}
