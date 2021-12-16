package com.github.silencecorner;

public class Sort {
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

    public static void selectOrder(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // 找出当前数组中的最小数字放到当前i的为止
            int tmpIndex = i;
            int value = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                if (value > nums[j]){
                    value = nums[j];
                    tmpIndex = j;
                }
            }
            if (tmpIndex != i){
                int tmp = nums[tmpIndex];
                nums[tmpIndex] = nums[i];
                nums[i] = tmp;
            }


        }
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

    /**
     * 冒泡排序，类似插入排序，每次处理一个最大的（每次拿到最大的，就像冒泡一样），
     * 插入排序为前n个为有序一旦发现后面的数据大于最后一项进行下一项处理
     */
    public static void bubbleSort(int[] nums) {
        int forCount = 0;
        for (int i = 0; i < nums.length; i++) {

            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                    forCount++;
                }
            }

        }

        System.out.printf("bubble sorting for count %d \n", forCount);

    }

}
