package io.github.jackie1in;

public class ArrayAlg {
    /**
     * 旋转数组暴力解法，每次移动一次
     * https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2skh7/
     * 输入: nums = [1,2,3,4,5,6,7], k = 3
     * 输出: [5,6,7,1,2,3,4]
     * 解释:
     * 向右轮转 1 步: [7,1,2,3,4,5,6]
     * 向右轮转 2 步: [6,7,1,2,3,4,5]
     * 向右轮转 3 步: [5,6,7,1,2,3,4]
     * @param nums 数组
     * @param k 旋转次数
     */
    public static void rotateForce(int[] nums, int k) {
        k = k % nums.length;
        while(k > 0){
            int tmp = nums[0];
            for (int i = 0; i < nums.length; i++){
                int t = tmp;
                tmp = nums[(i + 1) % nums.length];
                nums[(i + 1) % nums.length] = t;
            }
            k--;
        }
    }

    /**
     * 直接计算得到
     * 需要一个已访问列表
     * @param nums 数组
     * @param k 旋转次数
     */
    public static void rotateAccess(int[] nums, int k) {
        k %= nums.length;
        int i = nums.length;
        int j = 0;
        int tmp = nums[0];
        int[] access = new int[nums.length];
        while (i > 0) {
            int t = tmp;
            int index = (j + k) % nums.length;
            tmp = nums[index];
            nums[index] = t;
            access[index] = 1;
            j = index;
            if (access[(j + k) % nums.length] == 1 && j + 1 < nums.length){
                j++;
                tmp = nums[j];
            }
            i--;
        }
    }
    /**
     * 当循环一圈回到起始位置
     */
    public static void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        int count = gcd(k, n);
        for (int start = 0; start < count; ++start) {
            int current = start;
            int prev = nums[start];
            do {
                int next = (current + k) % n;
                int temp = nums[next];
                nums[next] = prev;
                prev = temp;
                current = next;
            } while (start != current);
        }
    }

    public static int gcd(int x, int y) {
        return y > 0 ? gcd(y, x % y) : x;
    }
}
