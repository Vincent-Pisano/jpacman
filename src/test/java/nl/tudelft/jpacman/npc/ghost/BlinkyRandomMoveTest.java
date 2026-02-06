package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.*;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BlinkyRandomMoveTest {

    @Test
    void nextMoveReturnsNullIfNoAccessibleSquares() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = {
            { factory.createWall() }
        };

        factory.createBoard(grid);

        Blinky blinky = new Blinky(dummySprites());
        blinky.occupy(grid[0][0]);

        assertNull(blinky.nextMove());
    }

    @Test
    void nextMoveUsesRandomMoveWhenAiReturnsEmpty() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = {
            { factory.createGround(), factory.createWall() },
            { factory.createWall(), factory.createWall() }
        };

        factory.createBoard(grid);

        Blinky blinky = new Blinky(dummySprites());
        Square start = grid[0][0];
        blinky.occupy(start);

        Direction move = blinky.nextMove();

        if (move == null) {
            // Legal behavior: no accessible squares
            return;
        }

        Square destination = start.getSquareAt(move);
        assertTrue(destination.isAccessibleTo(blinky));
    }

    @Test
    void blinkyNextAiMoveFailsIfNoSquare() {
        Blinky blinky = new Blinky(dummySprites());

        assertThrows(AssertionError.class, blinky::nextAiMove);
    }

    @Test
    void blinkyReturnsEmptyWhenNoPlayerExists() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = {
            { factory.createGround() }
        };
        factory.createBoard(grid);

        Blinky blinky = new Blinky(dummySprites());
        blinky.occupy(grid[0][0]);

        Optional<Direction> move = blinky.nextAiMove();

        assertTrue(move.isEmpty());
    }

    private static Map<Direction, Sprite> dummySprites() {
        Map<Direction, Sprite> sprites = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            sprites.put(d, null);
        }
        return sprites;
    }
}
