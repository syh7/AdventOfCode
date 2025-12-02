package syh.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {

    private val subject = Puzzle2()

    @Test
    fun a() {
        assertEquals("1227775554", subject.doA("test"))
        assertEquals("12599655151", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("4174379265", subject.doB("test"))
        assertEquals("20942028255", subject.doB("actual"))
    }
}
