package com.github.silencecorner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    private int[] nums;
    private static int[] numSorted;


    @BeforeAll
    public static void init0() {
        numSorted = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }
    @BeforeEach
    public void init1() {
        nums = new int[]{8, 10, 9, 11, 4, 7, 3, 5, 2, 1, 6};
    }


    @Test
    void shellSort() {
        Sort.shellSort(nums);
        assertArrayEquals(numSorted,nums);
    }

    @Test
    void selectOrder() {
        Sort.selectOrder(nums);
        assertArrayEquals(numSorted,nums);
    }

    @Test
    void insertSort() {
        Sort.insertSort(nums);
        assertArrayEquals(numSorted,nums);
    }

    @Test
    void bubbleSort() {
        Sort.bubbleSort(nums);
        assertArrayEquals(numSorted,nums);
    }
}