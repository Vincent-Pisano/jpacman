package nl.tudelft.jpacman.sprite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class SpriteStoreConditionCoverageTest {

    private SpriteStore store;

    @BeforeEach
    void setUp() {
        store = new SpriteStore();
    }

    @Test
    void loadSpriteUsesCacheBranch() throws Exception {
        // Arrange: pre-fill the internal cache so loadSprite() takes the "cache hit" branch.
        Sprite cached = new EmptySprite();

        Field f = SpriteStore.class.getDeclaredField("spriteMap");
        f.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Sprite> cache = (Map<String, Sprite>) f.get(store);

        cache.put("/does-not-matter.png", cached);

        // Act
        Sprite result = store.loadSprite("/does-not-matter.png");

        // Assert
        assertThat(result).isSameAs(cached);
    }

    @Test
    void createAnimatedSpriteRejectsNullBaseImage() {
        assertThatThrownBy(() -> store.createAnimatedSprite(null, 4, 0, false))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void createAnimatedSpriteRejectsNonPositiveFrames() {
        Sprite base = new ImageSprite(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));

        assertThatThrownBy(() -> store.createAnimatedSprite(base, 0, 0, false))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
