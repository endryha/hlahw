package com.hla.algos;

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class AVLTreeLauncher {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutting down")));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter AVL Tree size");

        int num;
        while ((num = scanner.nextInt()) > 0) {
            insertRandom(num);
            System.out.println("Enter AVL Tree size");
        }

        System.exit(1);
    }

    private static void insertRandom(int n) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        AVLTree tree = new AVLTree();

        for (int i = 0; i < n; i++) {
            int val = random.nextInt();
            tree.insert(val, val);
        }
    }

}
