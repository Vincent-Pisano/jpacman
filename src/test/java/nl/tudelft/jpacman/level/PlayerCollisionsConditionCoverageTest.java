package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PlayerCollisionsConditionCoverageTest {

    @Test
    void collidePlayerVsGhostKillsWhenLivesReachZero() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);

        when(player.getRemainingLives()).thenReturn(0);

        collisions.collide(player, ghost);

        verify(pc).collidedWithAGhost(player, ghost);
        verify(player).removeLives(1);
        verify(player).setAlive(false);
        verify(player).setKiller(ghost);
    }

    @Test
    void collidePlayerVsGhostDoesNotKillWhenLivesRemain() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);

        when(player.getRemainingLives()).thenReturn(2);

        collisions.collide(player, ghost);

        verify(pc).collidedWithAGhost(player, ghost);
        verify(player).removeLives(1);
        verify(player, never()).setAlive(false);
        verify(player, never()).setKiller(any());
    }

    @Test
    void collidePlayerVsPelletConsumesAndRemovesPellet() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);

        collisions.collide(player, pellet);

        verify(pc).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
        verifyNoMoreInteractions(pc);
    }

    @Test
    void collideGhostVsPlayerRoutesToPlayerVersusGhost() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Ghost ghost = mock(Ghost.class);
        Player player = mock(Player.class);

        when(player.getRemainingLives()).thenReturn(1);

        collisions.collide(ghost, player);

        verify(pc).collidedWithAGhost(player, ghost);
        verify(player).removeLives(1);
        verify(player, never()).setAlive(false);
        verify(player, never()).setKiller(any());
    }

    @Test
    void collidePelletVsPlayerRoutesToPlayerVersusPellet() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Pellet pellet = mock(Pellet.class);
        Player player = mock(Player.class);

        collisions.collide(pellet, player);

        verify(pc).consumedAPellet(player, pellet);
        verify(pellet).leaveSquare();
    }

    @Test
    void collideDoesNothingForUnknownMoverType() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Unit unknownMover = mock(Unit.class);
        Unit other = mock(Unit.class);

        collisions.collide(unknownMover, other);

        verifyZeroInteractions(pc);
    }

    @Test
    void collidePlayerWithNeitherGhostNorPelletDoesNothing() {
        PointCalculator pc = mock(PointCalculator.class);
        PlayerCollisions collisions = new PlayerCollisions(pc);

        Player player = mock(Player.class);
        Unit other = mock(Unit.class);

        collisions.collide(player, other);

        verifyZeroInteractions(pc);
        verify(player, never()).removeLives(anyInt());
    }
}
