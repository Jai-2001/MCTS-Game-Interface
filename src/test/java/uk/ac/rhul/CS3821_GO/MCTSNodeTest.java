package uk.ac.rhul.CS3821_GO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        root.add(childOne);
        root.add(childTwo);
        ArrayList<MCTSNode> children = root.getChildren();
        assertTrue(children.contains(childOne));
        assertTrue(children.contains(childTwo));
    }

    @Test
    void testPayloads(){
        root.setMoves(new ArrayList(List.of(new int[]{1, 1})));
        MCTSNode child = new MCTSNode();
        child.setMoves(new ArrayList<>(List.of(new int[]{1,1}, new int[]{2,2})));
        assertArrayEquals(root.getMoves().get(0), new int[]{1, 1});
        assertArrayEquals(child.getMoves().get(0), new int[]{1, 1});
        assertArrayEquals(child.getMoves().get(1), new int[]{2, 2});
    }

}