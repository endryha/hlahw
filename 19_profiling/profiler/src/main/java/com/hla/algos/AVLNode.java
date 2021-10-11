package com.hla.algos;

class AVLNode<T extends Comparable<T>, U extends Comparable<U>> {
    public AVLNode<T, U> left;
    public AVLNode<T, U> right;
    public AVLNode<T, U> parent;
    public T key;
    public int balance;
    public int height;
    public U value;

    public AVLNode(T k, U value) {
        left = right = parent = null;
        balance = 0;
        height = 0;
        key = k;
        this.value = value;
    }

    public String toString() {
        return "" + key + " --> " + value;
    }
}