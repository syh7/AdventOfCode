package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle25Test {

    private val subject = Puzzle25()

    @Test
    fun a() {
        assertEquals("2=-1=0", subject.doA("test"))
        assertEquals("20==1==12=0111=2--20", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("", subject.doB("test"))
        assertEquals("", subject.doB("actual"))
    }
}