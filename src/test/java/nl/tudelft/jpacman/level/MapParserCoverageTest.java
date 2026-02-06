package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapParserCoverageTest {

    private MapParser parser;

    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        LevelFactory levelFactory = new LevelFactory(
            sprites,
            new GhostFactory(sprites),
            mock(PointCalculator.class)
        );
        parser = new MapParser(levelFactory, new BoardFactory(sprites));
    }

    // --- checkMapFormat branches

    @Test
    void parseMapRejectsNullText() {
        assertThatThrownBy(() -> parser.parseMap((List<String>) null))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    void parseMapRejectsEmptyText() {
        assertThatThrownBy(() -> parser.parseMap(List.of()))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    void parseMapRejectsEmptyLines() {
        assertThatThrownBy(() -> parser.parseMap(List.of("")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    void parseMapRejectsUnequalLineWidths() {
        assertThatThrownBy(() -> parser.parseMap(List.of("###", "##")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    // --- addSquare switch cases

    @Test
    void parseMapCoversSpaceWallPelletGhostPlayer() {
        // One row containing: space, wall, pellet, ghost, player
        Level level = parser.parseMap(List.of(" #.GP"));
        Board board = level.getBoard();

        Square space = board.squareAt(0, 0);
        Square wall  = board.squareAt(1, 0);
        Square pelletSquare = board.squareAt(2, 0);
        Square ghostSquare  = board.squareAt(3, 0);
        Square playerStart  = board.squareAt(4, 0);

        assertThat(space.isAccessibleTo(mock(nl.tudelft.jpacman.board.Unit.class))).isTrue();
        assertThat(wall.isAccessibleTo(mock(nl.tudelft.jpacman.board.Unit.class))).isFalse();

        assertThat(pelletSquare.getOccupants())
            .anyMatch(u -> u instanceof Pellet);

        assertThat(ghostSquare.getOccupants())
            .anyMatch(u -> u instanceof Ghost);

        // Verify 'P' produced a start position by registering a player.
        Player player = mock(Player.class);
        level.registerPlayer(player);
        verify(player).occupy(playerStart);
    }

    @Test
    void parseMapRejectsInvalidCharacters() {
        assertThatThrownBy(() -> parser.parseMap(List.of("X")))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    void parseMapInputStreamEmptyTriggersFormatException() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        assertThatThrownBy(() -> parser.parseMap(in))
            .isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    void parseMapMissingResourceThrowsConfigurationException() {
        assertThatThrownBy(() -> parser.parseMap("/no_such_map_file.txt"))
            .isInstanceOf(PacmanConfigurationException.class);
    }
}
