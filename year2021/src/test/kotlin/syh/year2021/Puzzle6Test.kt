package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {

    private val subject = Puzzle6()

    @Test
    fun a() {
        assertEquals("5934", subject.doA("test"))
        assertEquals("389726", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("26984457539", subject.doB("test"))
        assertEquals("1743335992042", subject.doB("actual"))
    }
}