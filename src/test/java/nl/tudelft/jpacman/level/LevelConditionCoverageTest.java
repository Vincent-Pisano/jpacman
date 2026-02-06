package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelConditionCoverageTest {

    @Test
    void moveThrowsWhenUnitHasNoSquare() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        var collisions = mock(CollisionMap.class);
        var start = mock(Square.class);

        Level level = new Level(board, List.of(), List.of(start), collisions);

        Unit unit = mock(Unit.class);
        when(unit.hasSquare()).thenReturn(false);

        assertThatThrownBy(() -> level.move(unit, Direction.EAST))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void moveReturnsEarlyWhenNotInProgress() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        var collisions = mock(CollisionMap.class);
        var start = mock(Square.class);

        Level level = new Level(board, List.of(), List.of(start), collisions);

        Unit unit = mock(Unit.class);
        when(unit.hasSquare()).thenReturn(true);

        level.move(unit, Direction.EAST);

        verifyZeroInteractions(collisions);
        verify(unit, never()).occupy(any());
    }

    @Test
    void moveDoesNotEnterInaccessibleSquare() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        var collisions = mock(CollisionMap.class);
        var start = mock(Square.class);

        Level level = new Level(board, List.of(), List.of(start), collisions);
        level.start(); // inProgress = true

        Unit unit = mock(Unit.class);
        Square location = mock(Square.class);
        Square destination = mock(Square.class);

        when(unit.hasSquare()).thenReturn(true);
        when(unit.getSquare()).thenReturn(location);
        when(location.getSquareAt(Direction.EAST)).thenReturn(destination);
        when(destination.isAccessibleTo(unit)).thenReturn(false);

        level.move(unit, Direction.EAST);

        verify(unit, never()).occupy(destination);
        verifyZeroInteractions(collisions);
    }

    @Test
    void moveCollidesWithAllOccupantsWhenAccessible() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        var collisions = mock(CollisionMap.class);
        var start = mock(Square.class);

        Level level = new Level(board, List.of(), List.of(start), collisions);
        level.start();

        Unit mover = mock(Unit.class);
        Square location = mock(Square.class);
        Square destination = mock(Square.class);

        Unit occ1 = mock(Unit.class);
        Unit occ2 = mock(Unit.class);

        when(mover.hasSquare()).thenReturn(true);
        when(mover.getSquare()).thenReturn(location);
        when(location.getSquareAt(Direction.EAST)).thenReturn(destination);
        when(destination.isAccessibleTo(mover)).thenReturn(true);
        when(destination.getOccupants()).thenReturn(List.of(occ1, occ2));

        level.move(mover, Direction.EAST);

        verify(mover).occupy(destination);
        verify(collisions).collide(mover, occ1);
        verify(collisions).collide(mover, occ2);
    }

    @Test
    void startNotifiesLostWhenNoPlayersAliveAndWonWhenNoPellets() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        when(board.getWidth()).thenReturn(0);
        when(board.getHeight()).thenReturn(0);

        var collisions = mock(CollisionMap.class);
        var startSquare = mock(Square.class);

        Level level = new Level(board, List.of(), List.of(startSquare), collisions);

        Level.LevelObserver obs = mock(Level.LevelObserver.class);
        level.addObserver(obs);

        level.start();

        verify(obs).levelLost();
        verify(obs).levelWon();
    }

    @Test
    void registerPlayerThrowsWhenNoStartSquares() {
        var board = mock(nl.tudelft.jpacman.board.Board.class);
        var collisions = mock(CollisionMap.class);

        Level level = new Level(board, List.of(), List.of(), collisions);

        Player p = mock(Player.class);
        assertThatThrownBy(() -> level.registerPlayer(p))
            .isInstanceOf(IllegalStateException.class);
    }
}
