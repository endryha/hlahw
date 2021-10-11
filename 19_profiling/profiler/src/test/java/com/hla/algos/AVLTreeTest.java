package com.hla.algos;

import org.junit.Test;

import java.util.Random;

public class AVLTreeTest {

    /*@Test
    public void insert() {
        int n = 30_000_000;
        insertRandom(n);
    }

    @Test
    public void testBigO() {
        for (int i = 0; i < 10; i++) {
            insertRandom(i * 1_000_000);
        }
    }*/

    private void insertRandom(int n) {
        Random random = new Random();

        AVLTree tree = new AVLTree();

        for (int i = 0; i < n; i++) {
            int val = random.nextInt();
            tree.insert(val, val);
        }
    }

    @Test
    public void insertAndDelete() {
        int deleteRatio = 10;
        int n = 5_000_000;
        Random random = new Random();

        AVLTree tree = new AVLTree();

        Integer deleteNode = null;
        for (int i = 0; i < n; i++) {
            int val = random.nextInt();
            tree.insert(val, val);

            if (i > 0 && i % deleteRatio == 0) {
                if (deleteNode == null) {
                    deleteNode = val;
                    tree.remove(random.nextInt());
                } else {
                    tree.remove(deleteNode);
                    deleteNode = null;
                }
            }
        }
    }
}
