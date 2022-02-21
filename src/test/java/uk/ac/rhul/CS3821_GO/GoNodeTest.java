package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GoNodeTest {

    GoNode root;

    @BeforeEach
    void setUp() {
        GoNode root = new GoNode();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTraversal(){
        GoNode childOne = new GoNode();
        GoNode childTwo = new GoNode();
        root.add(childOne);
        root.add(childTwo);
        ArrayList<GoNode> children = root.getChildren();
        assertTrue(children.contains(childOne));
        assertTrue(children.contains(childTwo));
    }


}