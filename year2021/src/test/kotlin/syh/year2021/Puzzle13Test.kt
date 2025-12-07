package syh.year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {

    private val subject = Puzzle13()

    @Test
    fun a() {
        assertEquals("17", subject.doA("test"))
        assertEquals("689", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(
            "#####\n" +
                    "#...#\n" +
                    "#...#\n" +
                    "#...#\n" +
                    "#####\n", subject.doB("test")
        )
        assertEquals(
            "###..#....###...##....##..##..#....#..#\n" +
                    "#..#.#....#..#.#..#....#.#..#.#....#..#\n" +
                    "#..#.#....###..#.......#.#....#....#..#\n" +
                    "###..#....#..#.#.......#.#.##.#....#..#\n" +
                    "#.#..#....#..#.#..#.#..#.#..#.#....#..#\n" +
                    "#..#.####.###...##...##...###.####..##.\n", subject.doB("actual")
        )
    }
}