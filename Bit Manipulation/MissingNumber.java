// https://leetcode.com/problems/missing-number/description/

import java.util.Arrays;
public class MissingNumber {
    public static void main(String[] args) {

        /*
         Problem Statement:
         You are given an array nums containing n distinct numbers
         taken from the range [0, n].
         Exactly one number in this range is missing from the array.

         Your task is to find and return the missing number.


        */

        int[] nums = {3, 0, 1};

        // Brute-force approach: sort and compare indices, TC: O(n log n)
        missingNumberBrute(nums);

        // Better approach 1: sum formula, TC: O(n)
        missingNumberBetter1(nums);

        // Better approach 2: single-loop sum difference, TC: O(n)
        missingNumberBetter2(nums);

        // Optimized approach 1: XOR with two loops, TC: O(n)
        missingNumberOptimzied1(nums);

        // Optimized approach 2: XOR with single loop, TC: O(n)
        missingNumberOptimzied2(nums);

    }

    /*
     Brute-force Approach (Sorting):
     - Sort the array.
     - After sorting, each number should match its index
       because the array contains numbers from [0, n].
     - The first index where nums[i] != i is the missing number.
     - If all indices match, then the missing number is n.

     Example:
     nums = [0, 1, 3]
     index:  0  1  2
     value:  0  1  3  -> mismatch at index 2 → missing = 2

     Time Complexity: O(n log n)
     Space Complexity: O(1) (in-place sort)
    */
    private static int missingNumberBrute(int[] nums) {
        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {
            if (i != nums[i]) {
                return i;
            }
        }

        // If all elements match their indices,
        // then the missing number is n
        return nums.length;
    }

    /*
     Better Approach 1 (Mathematical Sum Formula):
     - The sum of numbers from 0 to n is:
           n * (n + 1) / 2
     - Compute the expected sum using the formula.
     - Compute the actual sum of the array elements.
     - The difference between expected and actual sum
       gives the missing number.

     Example:
     nums = [3, 0, 1]
     n = 3
     expectedSum = 3 * 4 / 2 = 6
     actualSum = 3 + 0 + 1 = 4
     missing = 6 - 4 = 2

     Time Complexity: O(n)
     Space Complexity: O(1)
    */
    private static int missingNumberBetter1(int[] nums) {
        int expectedSum = (nums.length * (nums.length + 1)) / 2;   // sumation formula: (n(n+1)) / 2  -> for n elements

        int actualSum = 0;
        for (int i = 0; i < nums.length; i++) {
            actualSum += nums[i];
        }

        return expectedSum - actualSum;
    }

    /*
     Better Approach 2 (Single Loop Sum Difference):
     - Instead of calculating expected and actual sums separately,
       we combine both in a single loop.
     - Add all indices and subtract all array values.
     - Initialize result with n (nums.length) to include the last number.

     Logic:
     res = n + (0 + 1 + 2 + ... + n-1) - (nums[0] + nums[1] + ...)

     Example:
     nums = [3, 0, 1]
     res starts at 3
     i=0 → res += 0 - 3 → res = 0
     i=1 → res += 1 - 0 → res = 1
     i=2 → res += 2 - 1 → res = 2

     Time Complexity: O(n)
     Space Complexity: O(1)
    */
    private static int missingNumberBetter2(int[] nums) {
        int res = nums.length;

        for (int i = 0; i < nums.length; i++) {
            res += i - nums[i];
        }

        return res;
    }

    /*
     Optimized Approach 1 (XOR with Two Loops):
     - XOR has useful properties:
         * a ^ a = 0
         * a ^ 0 = a
     - XOR all numbers from 0 to n.
     - XOR all elements of the array.
     - All matching numbers cancel out, leaving the missing number.

     Example:
     nums = [3, 0, 1]
     XOR of 0..3 = 0 ^ 1 ^ 2 ^ 3
     XOR of nums = 3 ^ 0 ^ 1
     Remaining value = 2

     Time Complexity: O(n)
     Space Complexity: O(1)
    */
    private static int missingNumberOptimzied1(int[] nums) {
        int res = 0;

        // XOR all numbers from 0 to n (including)
        for (int i = 0; i <= nums.length; i++) {
            res ^= i;
        }

        // XOR all elements in the array
        for (int i = 0; i < nums.length; i++) {
            res ^= nums[i];
        }

        return res;
    }

    /*
     Optimized Approach 2 (XOR with Single Loop):
     - This is a more compact version of the XOR approach.
     - Initialize result with n.
     - In one loop, XOR both index and array value.
     - Duplicate values cancel out, leaving the missing number.

     Time Complexity: O(n)
     Space Complexity: O(1)
    */
    private static int missingNumberOptimzied2(int[] nums) {
        int res = nums.length;

        for (int i = 0; i < nums.length; i++) {
            res ^= i;
            res ^= nums[i];
        }

        return res;
    }
}