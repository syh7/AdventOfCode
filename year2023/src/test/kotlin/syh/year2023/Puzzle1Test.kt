package syh.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {

    private val subject = Puzzle1()

    @Test
    fun a() {
        assertEquals("142", subject.doA("testA"))
        assertEquals("53921", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("281", subject.doB("testB"))
        assertEquals("54676", subject.doB("actual"))
    }
}