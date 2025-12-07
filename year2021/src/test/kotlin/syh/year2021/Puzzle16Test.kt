package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {

    private val subject = Puzzle16()

    @Test
    fun a() {
        assertEquals("6", subject.doA("testA"))
        assertEquals("9", subject.doA("testB"))
        assertEquals("14", subject.doA("testC"))
        assertEquals("16", subject.doA("testD"))
        assertEquals("12", subject.doA("testE"))
        assertEquals("23", subject.doA("testF"))
        assertEquals("31", subject.doA("testG"))
        assertEquals("977", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("3", subject.doB("testH"))
        assertEquals("54", subject.doB("testI"))
        assertEquals("7", subject.doB("testJ"))
        assertEquals("9", subject.doB("testK"))
        assertEquals("1", subject.doB("testL"))
        assertEquals("0", subject.doB("testM"))
        assertEquals("0", subject.doB("testN"))
        assertEquals("1", subject.doB("testO"))
        assertEquals("101501020883", subject.doB("actual"))
    }
}