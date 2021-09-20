package com.hla.algos;

import org.junit.Test;

public class AVLTreeTest {

    @Test
    public void preOrderTraversalTest() {
        AVLTree tree = new AVLTree();

        tree.root = tree.insert(tree.root, 9);
        tree.root = tree.insert(tree.root, 5);
        tree.root = tree.insert(tree.root, 10);
        tree.root = tree.insert(tree.root, 0);
        tree.root = tree.insert(tree.root, 6);
        tree.root = tree.insert(tree.root, 11);
        tree.root = tree.insert(tree.root, -1);
        tree.root = tree.insert(tree.root, 1);
        tree.root = tree.insert(tree.root, 2);

        /* The constructed AVL Tree would be
        9
        / \
        1 10
        / \ \
        0 5 11
        / / \
        -1 2 6
        */
        System.out.println("Preorder traversal of " +
                "constructed tree is : ");

        preOrder(tree.getRoot());

        tree.root = tree.deleteNode(tree.root, 10);

        /* The AVL Tree after deletion of 10
        1
        / \
        0 9
        /  / \
        -1 5 11
        / \
        2 6
        */
        System.out.println("");
        System.out.println("Preorder traversal after " +
                "deletion of 10 :");
        preOrder(tree.getRoot());
    }

    // A utility function to print preorder traversal of
    // the tree. The function also prints height of every
    // node
    private static void preOrder(AVLTree.Node node) {
        if (node != null) {
            System.out.print(node.getKey() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }
}
