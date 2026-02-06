package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

class UnitConditionCoverageTest {

    @Test
    void leaveSquareWhenNoSquareDoesNothing() {
        BasicUnit u = new BasicUnit();
        u.leaveSquare();
        assertThat(u.hasSquare()).isFalse();
    }

    @Test
    void getSquareAssertsWhenNoSquare() {
        BasicUnit u = new BasicUnit();
        assertThatThrownBy(u::getSquare)
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void getSquareAssertsWhenInvariantIsBroken() throws Exception {
        BasicUnit u = new BasicUnit();
        BasicSquare s = new BasicSquare();
        // Break invariant: set Unit.square without adding unit to square occupants.
        Field f = Unit.class.getDeclaredField("square");
        f.setAccessible(true);
        f.set(u, s);

        assertThatThrownBy(u::getSquare)
            .isInstanceOf(AssertionError.class);
    }

    @Test
    void squaresAheadOfCoversMultipleIterations() {
        BasicSquare s1 = new BasicSquare();
        BasicSquare s2 = new BasicSquare();
        BasicSquare s3 = new BasicSquare();
        s1.link(s2, Direction.EAST);
        s2.link(s3, Direction.EAST);
        s3.link(s3, Direction.EAST);

        BasicUnit u = new BasicUnit();
        u.occupy(s1);
        u.setDirection(Direction.EAST);

        assertThat(u.squaresAheadOf(2)).isEqualTo(s3);
    }
}
