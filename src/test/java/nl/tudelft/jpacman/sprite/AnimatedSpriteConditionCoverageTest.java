package nl.tudelft.jpacman.sprite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AnimatedSpriteConditionCoverageTest {

    @Test
    void constructorRejectsEmptyFrames() {
        assertThatThrownBy(() -> new AnimatedSprite(new Sprite[0], 1, true))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
