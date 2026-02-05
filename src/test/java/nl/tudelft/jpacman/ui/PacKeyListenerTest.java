package nl.tudelft.jpacman.ui;

import static org.mockito.Mockito.*;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verifyZeroInteractions;

class PacKeyListenerTest {

    @Test
    void testKeyPressedWithValidKey() {
        Action action = mock(Action.class);
        Map<Integer, Action> mappings = new HashMap<>();
        mappings.put(KeyEvent.VK_UP, action);

        PacKeyListener listener = new PacKeyListener(mappings);
        KeyEvent event = new KeyEvent(mock(Component.class), KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(), 0, KeyEvent.VK_UP, ' ');

        listener.keyPressed(event);
        verify(action, times(1)).doAction();
    }

    @Test
    void testKeyPressedWithUnknownKey() {
        Action action = mock(Action.class);
        Map<Integer, Action> mappings = new HashMap<>();
        mappings.put(KeyEvent.VK_UP, action);

        PacKeyListener listener = new PacKeyListener(mappings);

        KeyEvent event = new KeyEvent(mock(Component.class), KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, ' ');

        listener.keyPressed(event);
        verify(action, never()).doAction();
    }

    @Test
    void testKeyPressedNullEvent() {
        Action action = mock(Action.class);
        Map<Integer, Action> mappings = new HashMap<>();
        mappings.put(KeyEvent.VK_UP, action);

        PacKeyListener listener = new PacKeyListener(mappings);

        listener.keyPressed(null);

        verifyZeroInteractions(action);
    }
}
