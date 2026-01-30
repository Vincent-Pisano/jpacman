package nl.tudelft.jpacman.ui;

import java.util.Map;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A panel containing a button for every registered action.
 *
 * @author Jeroen Roosen 
 */
class ButtonPanel extends JPanel {

    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new button panel with a button for every action.
     * @param buttons The map of caption - action for each button.
     * @param parent The parent frame, used to return window focus.
     */
    ButtonPanel(final Map<String, Action> buttons, final JFrame parent) {
        super();

        Objects.requireNonNull(buttons, "buttons must not be null");
        Objects.requireNonNull(parent, "parent must not be null");

        for (Map.Entry<String, Action> entry : buttons.entrySet()) {
            String caption = entry.getKey();
            Action action = entry.getValue();

            JButton button = new JButton(caption);
            button.addActionListener(e -> {
                action.doAction();
                parent.requestFocusInWindow();
            });
            add(button);
        }
    }

}
