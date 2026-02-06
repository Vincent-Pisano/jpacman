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

class PinkyConditionCoverageTest {

    @Test
    void nextAiMoveReturnsEmptyWhenNoPlayerOnBoard() {
        BoardFactory factory = new BoardFactory(new PacManSprites());
        Square[][] grid = { { factory.createGround() } };
        factory.createBoard(grid);

        Pinky pinky = new Pinky(dummySprites());
        pinky.occupy(grid[0][0]);

        Optional<Direction> move = pinky.nextAiMove();
        assertThat(move).isEmpty();
    }

    @Test
    void nextAiMoveReturnsEmptyWhenDestinationIsInaccessible() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        Square[][] grid = new Square[][] {
            { factory.createGround() },
            { factory.createWall() }
        };
        factory.createBoard(grid);

        Pinky pinky = new Pinky(dummySprites());
        pinky.occupy(grid[0][0]);

        Player player = mock(Player.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        player.occupy(grid[1][0]);                 // wall
        player.setDirection(Direction.EAST);       // init Unit.direction

        Optional<Direction> move = pinky.nextAiMove();
        assertThat(move).isEmpty();
    }

    @Test
    void nextAiMoveReturnsFirstStepWhenPathExists() {
        BoardFactory factory = new BoardFactory(new PacManSprites());

        // 5 ground squares in a row (height=1)
        Square[][] grid = new Square[5][1];
        for (int x = 0; x < 5; x++) {
            grid[x][0] = factory.createGround();
        }
        factory.createBoard(grid);

        Player player = mock(Player.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        player.occupy(grid[0][0]);
        player.setDirection(Direction.EAST);

        Pinky pinky = new Pinky(dummySprites());
        pinky.occupy(grid[3][0]);

        // Player destination (4 ahead) is x=4, Pinky at x=3 => first move must be EAST.
        Optional<Direction> move = pinky.nextAiMove();
        assertThat(move).contains(Direction.EAST);
    }

    private static Map<Direction, Sprite> dummySprites() {
        Map<Direction, Sprite> sprites = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) {
            sprites.put(d, null);
        }
        return sprites;
    }
}
