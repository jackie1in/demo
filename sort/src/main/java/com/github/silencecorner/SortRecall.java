package com.github.silencecorner;

import javax.management.ValueExp;
import java.util.Arrays;
import java.util.Collections;

public class SortRecall {
    public static void bubbleSort(int[] arr) {
        // 将最大的交换到最后
        boolean swap = false;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // swap
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swap = true;
                }
            }
            if (!swap) {
                break;
            }
        }
    }


    public static void selectSort(int[] arr) {
        // 每次选出最小的放到相应位置
        for (int i = 0; i < arr.length; i++) {
            int min = arr[i];
            int idx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    min = arr[j];
                    idx = j;
                }
            }
            // swap
            arr[idx] = arr[i];
            arr[i] = min;
        }
    }

    public static void insertSort(int[] arr) {
        // 保持循环前段相对有序
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
    }

    public static void shellSort(int[] arr) {
        // 基本思想与插入排序一致,交换的步长arr / 2
        for (int step = arr.length / 2; step > 0; step /= 2) {
            for (int i = step; i < arr.length; i++) {
                int value = arr[i];
                int j;
                for (j = i - step; j >= 0 && arr[j] > value; j -= step) {
                    arr[j + step] = arr[j];
                }
                arr[j + step] = value;
            }
        }
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (null == arr
                || arr.length < 1
                || start >= end
                || start > arr.length - 1
                || end > arr.length - 1) {
            return;
        }
        // 选最后一个为基本pivot
        int pivot = arr[end];
        int p = end;
        int l = start;
        int r = end;

        while (r > l) {
            while (l < r && arr[l] < pivot) {
                l++;
            }
            while (l < r && arr[r] > pivot) {
                r--;
            }
            if (r > l) {
                // swap
                int temp = arr[r];
                arr[r] = arr[l];
                arr[l] = temp;
                l++;
                r--;
            }

        }
        if (arr[p] < arr[l]) {
            // swap pivot
            arr[p] = arr[l];
            arr[l] = pivot;
        }
        quickSort(arr, 0, l - 1);
        quickSort(arr, r + 1, p);
    }

    // 采用分治法
    public static void mergeSort(int[] arr) {
        int[] tmp = new int[arr.length];
        mergeSort(0, arr.length - 1, arr, tmp);
    }

    private static void mergeSort(int start, int end, int[] arr, int[] tmp) {
        int mid = (end + start) / 2;
        if (end > start) {
            mergeSort(start, mid, arr, tmp);
            mergeSort(mid + 1, end, arr, tmp);
            merge(start, mid, end, arr, tmp);
        }
    }

    private static void merge(int start, int mid, int end, int[] arr, int[] tmp) {
        if (start >= end) {
            return;
        }
        int l = start;
        int m = mid + 1;
        int k = 0;
        while (mid >= l && m <= end) {
            if (arr[l] > arr[m]) {
                tmp[k++] = arr[m++];
            } else {
                tmp[k++] = arr[l++];
            }
        }
        // 将剩下的拷贝到数组中
        while (l <= mid) {
            tmp[k++] = arr[l++];
        }
        while (m <= end) {
            tmp[k++] = arr[m++];
        }
        for (int x = 0; x < k; x++) {
            arr[start + x] = tmp[x];
        }

    }


}
