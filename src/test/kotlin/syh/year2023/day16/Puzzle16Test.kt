package syh.year2023.day16

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {

    private val subject = Puzzle16()

    @Test
    fun a() {
        assertEquals("46", subject.doA("test"))
        assertEquals("6921", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("51", subject.doB("test"))
        assertEquals("7594", subject.doB("actual"))
    }
}