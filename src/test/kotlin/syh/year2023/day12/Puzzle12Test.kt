package syh.year2023.day12

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {

    private val subject = Puzzle12()

    @Test
    fun a() {
        assertEquals(21, subject.doA("test"))
        assertEquals(7307, subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(525152, subject.doB("test"))
        assertEquals(3415570893842, subject.doB("actual"))
    }
}