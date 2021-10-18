package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntersectionTest {

    Intersection intersection;

    @BeforeEach
    void setUp() {
    intersection = new Intersection();
    }

    @Test
    void testInit(){
        assertEquals(StoneTypes.NONE, intersection.getStoneType());
    }
}
