package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {
    private val subject = Puzzle17()

    @Test
    fun a() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", subject.doA("test1"))
        assertEquals("4,2,5,6,7,7,7,7,3,1,0", subject.doA("test2"))
        assertEquals("1,5,0,3,7,3,0,3,1", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("117440", subject.doB("test3"))
        assertEquals("105981155568026", subject.doB("actual"))
    }
}