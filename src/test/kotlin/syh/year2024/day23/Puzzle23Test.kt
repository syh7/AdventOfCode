package syh.year2024.day23


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle23Test {
    private val subject = Puzzle23()

    @Test
    fun a() {
        assertEquals("7", subject.doA("test"))
        // 2311 too high
        assertEquals("1253", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals("co,de,ka,ta", subject.doB("test"))
        assertEquals("ag,bt,cq,da,hp,hs,mi,pa,qd,qe,qi,ri,uq", subject.doB("actual"))
    }
}