package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for DefaultPlayerInteractionMap.
 */
class DefaultPlayerInteractionMapTest {

    private DefaultPlayerInteractionMap defaultMap;
    private PointCalculator pointCalculator;

    @BeforeEach
    void setUp() {
        pointCalculator = mock(PointCalculator.class);
        defaultMap = new DefaultPlayerInteractionMap(pointCalculator);
    }

    @Test
    void testPlayerCollidesWithGhost() {
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);

        defaultMap.collide(player, ghost);

        verify(pointCalculator).collidedWithAGhost(player, ghost);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    @Test
    void testPlayerCollidesWithPellet() {
        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);

        defaultMap.collide(player, pellet);

        verify(pointCalculator).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }
}
