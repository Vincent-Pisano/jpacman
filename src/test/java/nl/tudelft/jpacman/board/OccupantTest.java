package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen 
 *
 */
class OccupantTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        assertThat(unit.hasSquare()).isFalse();
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        Square target = new BasicSquare();
        unit.occupy(target);

        assertThat(unit.hasSquare()).isTrue();
        assertThat(unit.getSquare()).isEqualTo(target);
        assertThat(target.getOccupants()).contains(unit);
    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        Square first = new BasicSquare();
        Square second = new BasicSquare();

        unit.occupy(first);
        unit.occupy(second);

        assertThat(first.getOccupants()).doesNotContain(unit);
        assertThat(second.getOccupants()).contains(unit);
        assertThat(unit.getSquare()).isEqualTo(second);
    }

    @Test
    void leaveSquareRemovesUnitFromSquare() {
        Square target = new BasicSquare();
        unit.occupy(target);

        unit.leaveSquare();

        assertThat(unit.hasSquare()).isFalse();
        assertThat(target.getOccupants()).doesNotContain(unit);
    }
}
