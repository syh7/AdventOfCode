package syh.year2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {
    private val subject = Puzzle10()

    @Test
    fun a() {
        assertEquals("13140", subject.doA("test"))
        assertEquals("15260", subject.doA("actual"))
    }

    @Test
    fun b() {
        assertEquals(
            "##  ##  ##  ##  ##  ##  ##  ##  ##  ##  \n" +
                    "###   ###   ###   ###   ###   ###   ### \n" +
                    "####    ####    ####    ####    ####    \n" +
                    "#####     #####     #####     #####     \n" +
                    "######      ######      ######      ####\n" +
                    "#######       #######       #######     \n ",
            subject.doB("test")
        )
        assertEquals(
            "###   ##  #  # ####  ##  #    #  #  ##  \n" +
                    "#  # #  # #  # #    #  # #    #  # #  # \n" +
                    "#  # #    #### ###  #    #    #  # #    \n" +
                    "###  # ## #  # #    # ## #    #  # # ## \n" +
                    "#    #  # #  # #    #  # #    #  # #  # \n" +
                    "#     ### #  # #     ### ####  ##   ### \n ",
            subject.doB("actual")
        )
    }
}