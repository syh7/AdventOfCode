package syh.year2022.day17

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {

    private val subject = Puzzle17()

    @Test
    fun a() {
        assertEquals("3068", subject.doA("test"))
        assertEquals("3191", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("1514285714288", subject.doB("test"))
        assertEquals("1572093023267", subject.doB("actual"))
    }
}