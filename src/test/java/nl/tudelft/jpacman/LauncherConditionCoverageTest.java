package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.ui.Action;
import nl.tudelft.jpacman.ui.PacManUI;
import nl.tudelft.jpacman.ui.PacManUiBuilder;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LauncherConditionCoverageTest {

    @Test
    void disposeBeforeLaunchThrowsAssertionError() {
        Launcher launcher = new Launcher();
        assertThatThrownBy(launcher::dispose)
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void disposeCallsPacManUIDisposeWhenInitialized() {
        Launcher launcher = new Launcher();
        PacManUI ui = mock(PacManUI.class);

        setPrivateField(launcher, "pacManUI", ui);

        launcher.dispose();

        verify(ui).dispose();
    }

    @Test
    void moveActionThrowsWhenGameHasNoPlayers() {
        Launcher launcher = new Launcher();
        RecordingBuilder builder = new RecordingBuilder();

        launcher.addSinglePlayerKeys(builder);
        Action up = builder.actions.get(KeyEvent.VK_UP);
        assertThat(up).isNotNull();

        Game game = mock(Game.class);
        when(game.getPlayers()).thenReturn(Collections.emptyList());

        setPrivateField(launcher, "game", game);

        assertThatThrownBy(up::doAction)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Game has 0 players");
    }

    @Test
    void moveActionMovesFirstPlayerWhenPlayersExist() {
        Launcher launcher = new Launcher();
        RecordingBuilder builder = new RecordingBuilder();

        launcher.addSinglePlayerKeys(builder);
        Action left = builder.actions.get(KeyEvent.VK_LEFT);
        assertThat(left).isNotNull();

        Game game = mock(Game.class);
        Player p = mock(Player.class);
        when(game.getPlayers()).thenReturn(List.of(p));

        setPrivateField(launcher, "game", game);

        left.doAction();

        verify(game).move(p, Direction.WEST);
    }

    @Test
    void makeLevelWrapsIOExceptionInPacmanConfigurationException() {
        Launcher launcher = new Launcher() {
            @Override
            protected MapParser getMapParser() {
                MapParser parser = mock(MapParser.class);
                try {
                    when(parser.parseMap(anyString())).thenThrow(new IOException("boom"));
                } catch (IOException e) {
                    throw new AssertionError("Mockito setup failed", e);
                }
                return parser;
            }

            @Override
            protected String getLevelMap() {
                return "/does-not-matter.txt";
            }
        };

        assertThatThrownBy(launcher::makeLevel)
            .isInstanceOf(PacmanConfigurationException.class)
            .hasCauseInstanceOf(IOException.class)
            .hasMessageContaining("Unable to create level");
    }

    /** Records key mappings without needing reflection into PacManUiBuilder internals. */
    private static final class RecordingBuilder extends PacManUiBuilder {
        private final Map<Integer, Action> actions = new HashMap<>();

        @Override
        public PacManUiBuilder addKey(Integer keyCode, Action action) {
            actions.put(keyCode, action);
            return super.addKey(keyCode, action);
        }
    }

    private static void setPrivateField(Launcher launcher, String fieldName, Object value) {
        try {
            Field f = Launcher.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(launcher, value);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Failed to set field: " + fieldName, e);
        }
    }
}
