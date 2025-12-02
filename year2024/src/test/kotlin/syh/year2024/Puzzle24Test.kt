package syh.year2024


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    private val subject = Puzzle24()

    @Test
    fun a() {
        assertEquals("4", subject.doA("test1"))
        assertEquals("2024", subject.doA("test2"))
        assertEquals("66055249060558", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("fcd,fhp,hmk,rvf,tpc,z16,z20,z33", subject.doB("actual"))
    }
}