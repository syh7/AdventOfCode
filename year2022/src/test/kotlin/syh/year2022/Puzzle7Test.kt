package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {
    private val subject = Puzzle7()

    @Test
    fun a() {
        assertEquals("95437", subject.doA("test"))
        assertEquals("1543140", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("24933642", subject.doB("test"))
        assertEquals("1117448", subject.doB("actual"))
    }
}