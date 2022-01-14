package com.github.silencecorner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    void bubbleSort() {
        SortRecall.bubbleSort(nums);
        assertArrayEquals(numSorted, nums);
        Sort.bubbleSort(numsBest);
        assertArrayEquals(numSorted, numsBest);
    }
    @Test
    void selectOrder() {
        SortRecall.selectSort(nums);
        assertArrayEquals(numSorted, nums);
    }
    @Test
    void insertSort() {
        SortRecall.insertSort(nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void shellSort() {
        System.out.println(this.getClass().getClassLoader());
        SortRecall.shellSort(nums);
        System.out.println(Arrays.toString(nums));
        assertArrayEquals(numSorted, nums);
    }


    @Test
    void quickSort() {
//        Sort.quickSort(nums, 0, nums.length - 1);
//        assertArrayEquals(numSorted, nums);
//        init1();
        SortRecall.quickSort(nums,0, nums.length - 1 );
        assertArrayEquals(numSorted, nums);

//        Sort.quickSort(numSorted, 0, nums.length - 1);
//        assertArrayEquals(numSorted, numSorted);
//        init1();
//        SortRecall.quickSort(numSorted,0, nums.length - 1 );
//        assertArrayEquals(numSorted, numSorted);
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
        Sort.mergeSort(nums);
        assertArrayEquals(numSorted, nums);
        init1();
        SortRecall.mergeSort(nums);
        assertArrayEquals(numSorted, nums);
    }

    @Test
    void radixSort() {
        // TODO 基数排序
        Sort.radixSort(nums);
        // assertArrayEquals(numSorted, nums);
    }
}