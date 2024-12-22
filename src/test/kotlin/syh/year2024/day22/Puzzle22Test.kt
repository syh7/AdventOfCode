package syh.year2024.day22


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle22Test {
    private val subject = Puzzle22()

    @Test
    fun a() {
        assertEquals("5908254", subject.doA("test1"))
        assertEquals("37327623", subject.doA("test2"))
        assertEquals("18525593556", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("9", subject.doB("test1"))
        assertEquals("23", subject.doB("test3"))
        assertEquals("2089", subject.doB("actual"))
    }
}