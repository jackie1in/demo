package com.github.silencecorner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    private int[] nums;
    private static int[] numSorted;

    private int[] numsBest;

    @BeforeAll
    public static void init0() {
        numSorted = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }

    @BeforeEach
    public void init1() {
        nums = new int[]{8, 10, 9, 11, 4, 7, 3, 5, 2, 1, 6};
        numsBest = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }


    @Test
    void shellSort() {
        System.out.println(this.getClass().getClassLoader());
        Sort.shellSort(nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void selectOrder() {
        Sort.selectOrder(nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void insertSort() {
        Sort.insertSort(nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void bubbleSort() {
        Sort.bubbleSort(nums);
        assertArrayEquals(numSorted, nums);
        Sort.bubbleSort(numsBest);
        assertArrayEquals(numSorted, numsBest);
    }

    @Test
    void quickSort() {
        Sort.quickSort(0, nums.length - 1, nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void quickSortFirst() {
        Sort.quickSortFirst(0, nums.length - 1, nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void quickSort1() {
        Sort.quickSort1(0, nums.length - 1, nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void mergeSort() {
        Sort.mergeSort(new int[]{4,2,3,1});
        // assertArrayEquals(numSorted, nums);
    }

    @Test
    void radixSort() {
        // TODO 基数排序
        Sort.radixSort(nums);
        // assertArrayEquals(numSorted, nums);
    }
}