package com.github.silencecorner;

import java.util.*;
import java.util.function.IntFunction;

public class Sort {

    /**
     * 冒泡排序，类似插入排序，每次处理一个最大的（每次拿到最大的，就像冒泡一样），
     * 插入排序为前n个为有序一旦发现后面的数据大于最后一项进行下一项处理 ，最差情况为O(n^2),最好情况是O(n),平均O(n^2)
     */
    public static void bubbleSort(int[] nums) {
        int forCount = 0;
        boolean didSwap = false;
        for (int i = 0; i < nums.length; i++) {

            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                    didSwap = true;
                }
                forCount++;
            }
            if (!didSwap) {
                break;
            }
        }

        System.out.printf("bubble sorting for count %d \n", forCount);

    }

    public static void insertSort(int[] nums) {
        int forCount = 0;
        if (nums == null || nums.length == 1) {
            return;
        }
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j > 0 && nums[j] < nums[j - 1]; j--) {
                int tmp = nums[j];
                nums[j] = nums[j - 1];
                nums[j - 1] = tmp;
                forCount++;
            }
        }

        System.out.printf("insert sorting for count %d \n", forCount);
    }

    public static void shellSort(int[] nums) {
        int forCount = 0;
        // 停止条件
        for (int step = (nums.length / 2); step >= 1; step /= 2) {
            for (int i = step; i < nums.length; i++) {
                int value = nums[i];
                int j;
                for (j = i - step; j >= 0 && nums[j] > value; j -= step) {
                    nums[j + step] = nums[j];
                    forCount++;
                }
                nums[j + step] = value;
            }


        }
        System.out.printf("shell sorting for count %d \n", forCount);
    }

    public static void selectSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // 找出当前数组中的最小数字放到当前i的为止
            int tmpIndex = i;
            int value = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                if (value > nums[j]) {
                    value = nums[j];
                    tmpIndex = j;
                }
            }
            if (tmpIndex != i) {
                int tmp = nums[tmpIndex];
                nums[tmpIndex] = nums[i];
                nums[i] = tmp;
            }


        }
    }



    /**
     * 快速排序
     *
     * @param nums 排序队列
     */
    public static void quickSort(int[] nums, int start, int end) {
        if (start >= end) {
            return;
        }
        // 基准值
        int pivot = nums[end];
        int left = start;
        int right = end - 1;
        // 结束条件时左右两个下标没有相遇
        while (right > left) {
            while (nums[left] < pivot && right > left) {
                left++;
            }
            while (nums[right] > pivot && right > left) {
                right--;
            }
            if (right > left) {
                // 交换
                int tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;
                left++;
                right--;
            }
        }
        if (nums[end] < nums[left]) {
            // 交换标记和
            int tmp = nums[left];
            nums[left] = pivot;
            nums[end] = tmp;
        } else {
            left++;
        }
        // 递归排序左边
        quickSort(nums, start, left - 1);
        // 递归排序右边
        quickSort( nums,left + 1, end);
    }

    public static void quickSortFirst(int start, int end, int[] nums) {
        if (start >= end) {
            return;
        }
        // 基准值
        int pivot = nums[start];
        int left = start + 1;
        int right = end;
        // 结束条件时左右两个下标没有相遇
        while (right > left) {
            while (nums[right] > pivot && right > left) {
                right--;
            }
            while (nums[left] < pivot && right > left) {
                left++;
            }
            if (right > left) {
                // 交换
                int tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;
                left++;
                right--;
            }
        }
        if (pivot > nums[right]) {
            // 交换标记和
            int tmp = nums[right];
            nums[right] = pivot;
            nums[start] = tmp;
        } else {
            right--;
        }
        // 递归排序左边
        quickSortFirst(start, right - 1, nums);
        // 递归排序右边
        quickSortFirst(right + 1, end, nums);
    }

    public static void quickSort1(int head, int tail, int[] arr) {
        if (head >= tail || arr == null || arr.length <= 1) {
            return;
        }
        int i = head, j = tail, pivot = arr[(head + tail) / 2];
        while (i <= j) {
            while (arr[i] < pivot) {
                ++i;
            }
            while (arr[j] > pivot) {
                --j;
            }
            if (i < j) {
                int t = arr[i];
                arr[i] = arr[j];
                arr[j] = t;
                ++i;
                --j;
            } else if (i == j) {
                ++i;
            }
        }
        quickSort1(head, j, arr);
        quickSort1(i, tail, arr);
    }

    /**
     * 归并排序
     */
    public static void mergeSort(int[] nums) {
        int[] tmp = new int[nums.length];
        mergeSort(0, nums.length - 1, nums, tmp);
    }

    private static void mergeSort(int start, int end, int[] nums, int[] tmp) {
        int mid = (start + end) / 2;
        if (start < end) {
            mergeSort(start, mid, nums, tmp);
            mergeSort(mid + 1, end, nums, tmp);
            merge(nums, start, mid, end, tmp);
        }
    }


    /**
     * 过程:
     * <p>
     * 假如有[4,2,3,1] 待排序的数组 <br>
     * 先分解为一个一个的数字 <br>
     * 第一次 0 + 3 / 2 = 1：4,2 3,1 start 0 mid 1 end 3 <br>
     * 第二次 0 + 1 / 2 = 0： 4 2 3,1 start 0 mid 0 end 1 <br>
     * 第三次 1 + 1 + 3 / 2 = 2   4 2 3 1 start 1 + 1 mid 2 end 3 <br>
     * </p>
     * <table>
     * <tr>
     *    <th>start</th>
     *    <th>mid</th>
     *    <th>end</th>
     *    <th>sub array</th>
     *  </tr>
     * <tr>
     * <tr>
     *    <td>0</td>
     *    <td>1</td>
     *    <td>3</td>
     *    <td>[4,2] [3,1] </td>
     *    </tr>
     *    <tr>
     *    <td>0</td>
     *    <td>0</td>
     *    <td>1</td>
     *    <td>[4] [2] [3,1] </td>
     *    </tr>
     *    <tr>
     *    <td>2</td>
     *    <td>2</td>
     *    <td>3</td>
     *    <td>[4] [2] [3] [1] </td>
     *    </tr>
     *    </table>
     * 开始合并：
     *  <p>
     * 获取到较小值放到缓存数组中，后面的一次当道缓存数组中，然后放回到原始数组中去 <br>
     * 第一次合并： 4 > 2 => 2,4  [2,4] <br>
     * 第二次合并： 1 > 3 => 1,3 [1,3] <br>
     * 第三次合并： 1 > 2 ,3 > 4=> 1,2,3,4 [1,2,3,4] <br>
     * </p>
     *
     * @param nums  原数组
     * @param start 合并的开始坐标
     * @param mid   合并的开始坐标
     * @param end   合并的结束坐标
     * @param tmp   缓存数组
     */
    private static void merge(int[] nums, int start, int mid, int end, int[] tmp) {
        // 至少需要两个元素比较
        if (start >= end) {
            return;
        }
        int i = start, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= end) {
            if (nums[i] < nums[j]) {
                tmp[k++] = nums[i++];
            } else {
                tmp[k++] = nums[j++];
            }
        }
        while (i <= mid) {
            tmp[k++] = nums[i++];
        }

        while (j <= end) {
            tmp[k++] = nums[j++];
        }
        for (int x = 0; x < k; x++) {
            nums[start + x] = tmp[x];
        }
        System.out.printf("start: %d mid: %d end: %d tmp: %s\n", start, mid, end, Arrays.toString(tmp));
    }


    public static void radixSort(int[] nums) {

    }
}
