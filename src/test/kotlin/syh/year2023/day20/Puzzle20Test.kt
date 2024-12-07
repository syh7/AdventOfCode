package syh.year2023.day20

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle20Test {

    private val subject = Puzzle20()

    @Test
    fun a() {
        assertEquals(11687500, subject.doA("test"))
        assertEquals(807069600, subject.doA("actual"))
    }

    @Test
    fun b() {
//        assertEquals(46, subject.doB("test"))
        assertEquals(221453937522197, subject.doB("actual"))
    }
}