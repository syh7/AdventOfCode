package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {

    private val subject = Puzzle6()

    @Test
    fun a() {
        assertEquals("4277556", subject.doA("test"))
        assertEquals("5667835681547", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3263827", subject.doB("test"))
        assertEquals("9434900032651", subject.doB("actual"))
    }
}
