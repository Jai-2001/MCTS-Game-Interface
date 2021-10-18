package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    Intersection intersection;

    @BeforeEach
    void setUp() {
    intersection = new Intersection();
    }

    @Test
    void testInit(){
        assert(intersection.isCleared());
    }

    @Test
    void testFields(){
        intersection.setWhite();
        assert(intersection.isWhite());
        intersection.setBlack();
        assert(intersection.isBlack());
        intersection.clear();
        assert(intersection.isCleared());
    }
}
