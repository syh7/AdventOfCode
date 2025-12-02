package syh.library

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DirectionTest {

    @Test
    fun left() {
        assertEquals(Direction.NORTH, Direction.EAST.left90())
        assertEquals(Direction.EAST, Direction.SOUTH.left90())
        assertEquals(Direction.SOUTH, Direction.WEST.left90())
        assertEquals(Direction.WEST, Direction.NORTH.left90())
    }

    @Test
    fun right() {
        assertEquals(Direction.NORTH, Direction.WEST.right90())
        assertEquals(Direction.EAST, Direction.NORTH.right90())
        assertEquals(Direction.SOUTH, Direction.EAST.right90())
        assertEquals(Direction.WEST, Direction.SOUTH.right90())
    }

    @Test
    fun opposite() {
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite())
        assertEquals(Direction.EAST, Direction.WEST.opposite())
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite())
        assertEquals(Direction.WEST, Direction.EAST.opposite())
        assertEquals(Direction.NORTH_EAST, Direction.SOUTH_WEST.opposite())
        assertEquals(Direction.SOUTH_EAST, Direction.NORTH_WEST.opposite())
        assertEquals(Direction.SOUTH_WEST, Direction.NORTH_EAST.opposite())
        assertEquals(Direction.NORTH_WEST, Direction.SOUTH_EAST.opposite())
    }

}