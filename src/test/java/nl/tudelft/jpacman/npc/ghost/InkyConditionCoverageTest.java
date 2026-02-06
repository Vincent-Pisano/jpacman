package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InkyConditionCoverageTest {

    @Test
    void nextAiMoveReturnsEmptyWhenPlayerMissing() {
        BoardFactory factory = new BoardFactory(new PacManSprites());
        Square[][] grid = { { factory.createGround() } };
        factory.createBoard(grid);

        Inky inky = new Inky(dummySprites());
        Blinky blinky = new Blinky(dummySprites());

        inky.occupy(grid[0][0]);
        blinky.occupy(grid[0][0]);

        Optional<Direction> move = inky.nextAiMove();
        assertThat(move).isEmpty();
    }

    @Test
    void nextAiMoveReturnsEmptyWhenBlinkyMissing() {
        BoardFactory factory = new BoardFactory(new PacManSprites());
        Square[][] grid = { { factory.createGround() } };
        factory.createBoard(grid);

        Inky inky = new Inky(dummySprites());
        inky.occupy(grid[0][0]);

        Player player = mock(Player.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        player.occupy(grid[0][0]);

        Optional<Direction> move = inky.nextAiMove();
        assertThat(move).isEmpty();
    }

    @Test
    void nextAiMoveReturnsDirectionWhenBothBlinkyAndPlayerExist() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = new Square[5][1];
        for (int x = 0; x < 5; x++) {
            grid[x][0] = factory.createGround();
        }
        factory.createBoard(grid);

        // Player at x=0; Blinky at x=4; Inky at x=1 -> deterministic first move WEST (towards x=0).
        Player player = mock(Player.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        player.occupy(grid[0][0]);
        player.setDirection(Direction.EAST);

        Blinky blinky = new Blinky(dummySprites());
        blinky.occupy(grid[4][0]);

        Inky inky = new Inky(dummySprites());
        inky.occupy(grid[1][0]);

        Optional<Direction> move = inky.nextAiMove();
        assertThat(move).contains(Direction.WEST);
    }

    @Test
    void nextAiMoveReturnsEmptyWhenComputedDestinationEqualsCurrentSquare() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = new Square[5][1];
        for (int x = 0; x < 5; x++) {
            grid[x][0] = factory.createGround();
        }
        factory.createBoard(grid);

        // Make playerDestination == inkySquare (player at 0, SQUARES_AHEAD=2 => destination=2).
        Player player = mock(Player.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        player.occupy(grid[0][0]);
        player.setDirection(Direction.EAST);

        // Put Blinky on same square as destination so firstHalf is empty and destination stays == inkySquare.
        Blinky blinky = new Blinky(dummySprites());
        blinky.occupy(grid[2][0]);

        Inky inky = new Inky(dummySprites());
        inky.occupy(grid[2][0]);

        Optional<Direction> move = inky.nextAiMove();
        assertThat(move).isEmpty();
    }

    private static Map<Direction, Sprite> dummySprites() {
        Map<Direction, Sprite> sprites = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            sprites.put(d, null);
        }
        return sprites;
    }
}
