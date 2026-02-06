package nl.tudelft.jpacman.sprite;

import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class AnimatedSpriteConditionCoverageMoreTest {

    private static final class DummySprite implements Sprite {
        private final int w;
        private final int h;

        DummySprite(int w, int h) {
            this.w = w;
            this.h = h;
        }

        @Override
        public void draw(Graphics graphics, int x, int y, int width, int height) {
            // no-op
        }

        @Override
        public Sprite split(int x, int y, int width, int height) {
            return this;
        }

        @Override
        public int getWidth() {
            return w;
        }

        @Override
        public int getHeight() {
            return h;
        }
    }

    @Test
    void getWidthReturnsEndOfLoopWhenCurrentPastFrames() throws Exception {
        Sprite[] frames = { new DummySprite(7, 9), new DummySprite(7, 9) };
        AnimatedSprite sprite = new AnimatedSprite(frames, 1000, true, false);

        setInt(sprite, "current", frames.length); // force current >= length

        // END_OF_LOOP is EmptySprite, which is expected to be 0x0.
        assertThat(sprite.getWidth()).isZero();
        assertThat(sprite.getHeight()).isZero();
    }

    @Test
    void updateWhenNotAnimatingSetsLastUpdateToNow() throws Exception {
        Sprite[] frames = { new DummySprite(7, 9) };
        AnimatedSprite sprite = new AnimatedSprite(frames, 1000, true, false);

        setLong(sprite, "lastUpdate", 0L);

        long before = System.currentTimeMillis();
        sprite.split(0, 0, 1, 1); // calls update()

        long after = getLong(sprite, "lastUpdate");
        assertThat(after).isGreaterThanOrEqualTo(before);
    }

    @Test
    void nonLoopingStopsAnimatingAtEndOfFrames() throws Exception {
        Sprite[] frames = { new DummySprite(7, 9), new DummySprite(7, 9) };
        int delay = 1_000_000; // big delay to make exactly one update step deterministic

        AnimatedSprite sprite = new AnimatedSprite(frames, delay, false, true);

        // current one before end; one update step will set current == frames.length and animating=false
        setInt(sprite, "current", frames.length - 1);

        long now = System.currentTimeMillis();
        setLong(sprite, "lastUpdate", now - delay + 1);

        sprite.draw(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics(), 0, 0, 1, 1);

        assertThat(getBoolean(sprite, "animating")).isFalse();

        // After reaching the end, currentSprite() returns END_OF_LOOP.
        assertThat(sprite.getWidth()).isZero();
        assertThat(sprite.getHeight()).isZero();
    }

    @Test
    void loopingWrapsCurrentIndexWithModulo() throws Exception {
        Sprite[] frames = { new DummySprite(7, 9), new DummySprite(7, 9) };
        int delay = 1_000_000;

        AnimatedSprite sprite = new AnimatedSprite(frames, delay, true, true);

        // Start at last frame; one update step => current becomes 2 then modulo => 0
        setInt(sprite, "current", frames.length - 1);

        long now = System.currentTimeMillis();
        setLong(sprite, "lastUpdate", now - delay + 1);

        sprite.split(0, 0, 1, 1); // calls update()

        assertThat(getInt(sprite, "current")).isZero();
    }

    // --- reflection helpers

    private static void setInt(Object o, String field, int value) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.setInt(o, value);
    }

    private static int getInt(Object o, String field) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.getInt(o);
    }

    private static void setLong(Object o, String field, long value) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.setLong(o, value);
    }

    private static long getLong(Object o, String field) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.getLong(o);
    }

    private static boolean getBoolean(Object o, String field) throws Exception {
        Field f = o.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.getBoolean(o);
    }
}
