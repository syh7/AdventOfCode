package syh.year2023.day25

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle25Test {

    private val subject = Puzzle25()

    @Test
    fun a() {
        assertEquals("54", subject.doA("test"))
        assertEquals("600225", subject.doA("actual"))
    }
}