package syh.year2022.day5

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {

    private val subject = Puzzle5()

    @Test
    fun a() {
        assertEquals("CMZ", subject.doA("test"))
        assertEquals("SVFDLGLWV", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("MCD", subject.doB("test"))
        assertEquals("DCVTCVPCL", subject.doB("actual"))
    }
}