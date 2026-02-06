package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SquareConditionCoverageTest {

    private ExposedSquare square;

    /** Exposes the protected invariant() for testing boolean sub-conditions. */
    private static final class ExposedSquare extends BasicSquare {
        boolean checkInvariant() {
            return super.invariant(); // if your Square has invariant() with no args (as in jpacman)
        }
    }

    @BeforeEach
    void setUp() {
        square = new ExposedSquare();
    }

    @Test
    void putAssertsOccupantNotNull() {
        assertThatThrownBy(() -> square.put(null))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void putAssertsOccupantNotAlreadyPresent() {
        Unit u = mock(Unit.class);
        square.put(u);

        assertThatThrownBy(() -> square.put(u))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void removeAssertsOccupantNotNull() {
        assertThatThrownBy(() -> square.remove(null))
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void invariantTrueWhenOccupantHasNoSquare() {
        Unit u = mock(Unit.class);
        when(u.hasSquare()).thenReturn(false);

        square.put(u);

        assertThat(square.checkInvariant()).isTrue();
    }

    @Test
    void invariantTrueWhenOccupantSquareIsThisSquare() {
        Unit u = mock(Unit.class);
        when(u.hasSquare()).thenReturn(true);
        when(u.getSquare()).thenReturn(square);

        square.put(u);

        assertThat(square.checkInvariant()).isTrue();
    }

    @Test
    void invariantFalseWhenOccupantClaimsDifferentSquare() {
        Unit u = mock(Unit.class);
        when(u.hasSquare()).thenReturn(true);
        when(u.getSquare()).thenReturn(new BasicSquare()); // different instance

        square.put(u);

        assertThat(square.checkInvariant()).isFalse();
    }
}
