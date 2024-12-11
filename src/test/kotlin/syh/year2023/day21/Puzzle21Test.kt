package syh.year2023.day21

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {

    private val subject = Puzzle21()

    @Test
    fun a() {
//        assertEquals("6", subject.doA("test")) with 6 steps instead of 64
        assertEquals("3858", subject.doA("actual"))
    }

    @Test
    fun b() {
//        assertEquals("16", subject.doB("test")) with 6 steps instead of 26501365
        assertEquals("636350496972143", subject.doB("actual"))
    }
}