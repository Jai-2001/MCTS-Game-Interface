package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MCTSNodeTest {

    MCTSNode root;

    @BeforeEach
    void setUp() {
        root = new MCTSNode();
    }

    @AfterEach
    void tearDown() {
        root = null;
    }

    @Test
    void testTraversal(){
        MCTSNode childOne = new MCTSNode();
        MCTSNode childTwo = new MCTSNode();
        childTwo.setMove(new byte[]{1,1});
        root.add(childOne);
        root.add(childTwo);
        Collection<MCTSNode> children = root.getChildren().values();
        assertTrue(children.contains(childOne));
        assertTrue(children.contains(childTwo));
    }

}