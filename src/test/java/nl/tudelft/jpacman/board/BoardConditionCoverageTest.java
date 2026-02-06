package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BoardConditionCoverageTest {

    @Test
    void constructorAssertsGridNotNull() {
        assertThatThrownBy(() -> new Board(null))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void constructorAssertsNoNullSquaresInGrid() {
        Square ok = mock(Square.class);
        Square[][] grid = new Square[][] { { ok, null } }; // width=1 height=2
        assertThatThrownBy(() -> new Board(grid))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void squareAtAssertsReturnedSquareNotNull() throws Exception {
        Square a = mock(Square.class);
        Square b = mock(Square.class);
        Square[][] grid = new Square[][] { { a, b } };
        Board board = new Board(grid);

        // Mutate internal array *after* construction to hit assert result != null.
        Field f = Board.class.getDeclaredField("gameboard");
        f.setAccessible(true);
        Square[][] internal = (Square[][]) f.get(board);
        internal[0][0] = null;

        assertThatThrownBy(() -> board.squareAt(0, 0))
            .isInstanceOf(AssertionError.class);
    }
}
