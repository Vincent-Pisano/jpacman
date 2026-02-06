package nl.tudelft.jpacman.sprite;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;

class ImageSpriteConditionCoverageTest {

    @Test
    void splitWithNegativeCoordinatesHitsWithinImageFalseBranch() {
        Sprite sprite = new ImageSprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));

        Sprite split = sprite.split(-1, 0, 1, 1);

        assertThat(split).isInstanceOf(EmptySprite.class);
    }
}
