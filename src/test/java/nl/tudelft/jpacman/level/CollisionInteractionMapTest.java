package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class CollisionInteractionMapTest {

    static class A extends Unit { @Override public Sprite getSprite() { return null; } }
    static class B extends Unit { @Override public Sprite getSprite() { return null; } }
    static class SubA extends A { }

    @Test
    void collideDoesNothingWhenNoHandlersExist() {
        CollisionInteractionMap map = new CollisionInteractionMap();

        assertThatCode(() -> map.collide(new A(), new B()))
        .doesNotThrowAnyException();
    }

    @Test
    void collideReturnsWhenColliderTypeNotRegistered() {
        CollisionInteractionMap map = new CollisionInteractionMap();
        AtomicInteger calls = new AtomicInteger();

        map.onCollision(A.class, B.class, false, (a, b) -> calls.incrementAndGet());

        map.collide(new B(), new A()); // collider B not registered
        assertThat(calls.get()).isZero();
    }

    @Test
    void collideReturnsWhenCollideeTypeNotRegistered() {
        CollisionInteractionMap map = new CollisionInteractionMap();
        AtomicInteger calls = new AtomicInteger();

        map.onCollision(A.class, B.class, false, (a, b) -> calls.incrementAndGet());

        map.collide(new A(), new A()); // collidee A not registered under collider A
        assertThat(calls.get()).isZero();
    }

    @Test
    void collideReturnsWhenHandlerIsNull() {
        CollisionInteractionMap map = new CollisionInteractionMap();
        AtomicInteger calls = new AtomicInteger();

        map.onCollision(A.class, B.class, false,
            (CollisionInteractionMap.CollisionHandler<A, B>) null);
        map.collide(new A(), new B());

        assertThat(calls.get()).isZero(); // explicit assertion
    }


    @Test
    void symmetricCollisionAddsInverseHandler() {
        CollisionInteractionMap map = new CollisionInteractionMap();
        AtomicInteger calls = new AtomicInteger();

        map.onCollision(A.class, B.class, true, (a, b) -> calls.incrementAndGet());

        map.collide(new A(), new B());
        map.collide(new B(), new A()); // inverse path

        assertThat(calls.get()).isEqualTo(2);
    }

    @Test
    void inheritanceResolutionUsesRegisteredSuperclassKey() {
        CollisionInteractionMap map = new CollisionInteractionMap();
        AtomicInteger calls = new AtomicInteger();

        map.onCollision(A.class, B.class, false, (a, b) -> calls.incrementAndGet());

        map.collide(new SubA(), new B()); // SubA should resolve to A
        assertThat(calls.get()).isEqualTo(1);
    }
}
