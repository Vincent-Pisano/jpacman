package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnitSquaresAheadTest {

    @Test
    void squaresAheadOfZeroReturnsCurrentSquare() {
        BasicSquare s1 = new BasicSquare();
        BasicUnit u = new BasicUnit();
        u.occupy(s1);

        assertThat(u.squaresAheadOf(0)).isEqualTo(s1);
    }

    @Test
    void squaresAheadOfFollowsDirection() {
        BasicSquare s1 = new BasicSquare();
        BasicSquare s2 = new BasicSquare();
        s1.link(s2, Direction.EAST);
        s2.link(s2, Direction.EAST); // stay there

        BasicUnit u = new BasicUnit();
        u.occupy(s1);
        u.setDirection(Direction.EAST);

        assertThat(u.squaresAheadOf(1)).isEqualTo(s2);
    }
}
