package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SinglePlayerGameConditionCoverageTest {

    @Test
    void constructorAssertsPlayerNotNull() {
        Level level = mock(Level.class);
        PointCalculator pc = mock(PointCalculator.class);

        assertThatThrownBy(() -> new SinglePlayerGame(null, level, pc))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void constructorAssertsLevelNotNull() {
        Player player = mock(Player.class);
        PointCalculator pc = mock(PointCalculator.class);

        assertThatThrownBy(() -> new SinglePlayerGame(player, null, pc))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void constructorRegistersPlayerAndGettersReturnExpected() {
        Player player = mock(Player.class);
        Level level = mock(Level.class);
        PointCalculator pc = mock(PointCalculator.class);

        SinglePlayerGame game = new SinglePlayerGame(player, level, pc);

        verify(level).registerPlayer(player);
        assertThat(game.getPlayers()).containsExactly(player);
        assertThat(game.getLevel()).isSameAs(level);
    }
}
