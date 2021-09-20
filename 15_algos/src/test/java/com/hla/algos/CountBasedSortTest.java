package com.hla.algos;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class CountBasedSortTest {
    private static final int ITERATIONS = 5;

    @Test
    public void testRandomArray() {
        System.out.println("Sort random arrays");
        int inputSize = 10_000;
        long countSortTotal = 0;
        long quickSortTotal = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long countSortStart = System.currentTimeMillis();
            CountBasedSort.sort(randomArray(inputSize));
            long countSortElapsed = System.currentTimeMillis() - countSortStart;
            countSortTotal += countSortElapsed;

            long quickSortStart = System.currentTimeMillis();
            Arrays.sort(randomArray(inputSize));
            long quickSortElapsed = System.currentTimeMillis() - quickSortStart;
            quickSortTotal += quickSortElapsed;

            System.out.println("Iteration #" + (i + 1));
            System.out.println("Count Sort = " + countSortElapsed + " ms");
            System.out.println("Quick Sort = " + quickSortElapsed + " ms");
        }
        System.out.println("Summary:");
        System.out.println("Count Sort Avg = " + (countSortTotal / ITERATIONS) + " ms");
        System.out.println("Quick Sort Avg = " + (quickSortTotal / ITERATIONS) + " ms");
    }

    @Test
    public void testLowCardinalityArray() {
        System.out.println("Sort low cardinality arrays");
        int inputSize = 100_000_000;
        long countSortTotal = 0;
        long quickSortTotal = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long countSortStart = System.currentTimeMillis();
            CountBasedSort.sort(lowCardinalityArray(inputSize));
            long countSortElapsed = System.currentTimeMillis() - countSortStart;
            countSortTotal += countSortElapsed;

            long quickSortStart = System.currentTimeMillis();
            Arrays.sort(lowCardinalityArray(inputSize));
            long quickSortElapsed = System.currentTimeMillis() - quickSortStart;
            quickSortTotal += quickSortElapsed;

            System.out.println("Iteration #" + (i + 1));
            System.out.println("Count Sort = " + countSortElapsed + " ms");
            System.out.println("Quick Sort = " + quickSortElapsed + " ms");
        }
        System.out.println("Summary:");
        System.out.println("Count Sort Avg = " + (countSortTotal / ITERATIONS) + " ms");
        System.out.println("Quick Sort Avg = " + (quickSortTotal / ITERATIONS) + " ms");
    }


    private static int[] randomArray(int n) {
        Random rd = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Math.abs(rd.nextInt());
        }
        return arr;
    }

    private static int[] lowCardinalityArray(int n) {
        Random rd = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Math.abs(rd.nextInt() % 10);
        }
        return arr;
    }
}
