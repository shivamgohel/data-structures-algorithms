// https://leetcode.com/problems/single-number/description/

import java.util.Arrays;
import java.util.HashMap;

class SingleNumber {
    public static void main() {


        /*
         Problem Statement:
         You are given a non-empty array of integers `nums` where every element appears twice
         except for one element which appears exactly once.

         Return the element that appears only once.

         Constraints:
         - Time complexity must be O(n) for the optimal solution.
         - You must solve it using constant extra space.
        */

        int[] nums = {4,1,2,1,2};

        // Brute-force approach: Frequency count by nested loops (O(n^2))
        System.out.println(singleNumberBrute(nums));

        // Better approach #1: Sort then check pairs (O(n log n))
        System.out.println(singleNumberBetter1(nums));

        // Better approach #2: HashMap frequency count (O(n))
        System.out.println(singleNumberBetter2(nums));

        // Optimized approach: XOR trick (O(n), O(1) space)
        System.out.println(singleNumberOptimized(nums));

    }


    /*
      Brute-force Approach:
      - Check each element against every other element and count its frequency.
      - If any element appears exactly once, return it.

      Approach Steps:
      1. Iterate through each element `i` in the array.
      2. For each `i`, loop again to count how many times it appears.
      3. If frequency is exactly 1, return that element.

      Time Complexity: O(n^2) — nested loops for frequency counting.
      Space Complexity: O(1) — constant extra variables only.
     */
    private static int singleNumberBrute(int[] nums) {
        if (nums.length == 1)
            return nums[0];

        for(int i=0; i<nums.length; i++){
            int count = 0;
            for(int j=0; j<nums.length; j++){
                if(nums[i] == nums[j])
                    count++;
            }

            if(count == 1){
                return nums[i];
            }
        }

        return -1;
    }

    /*
     Better Approach #1 (Sorting):
     - After sorting, all duplicate elements will appear next to each other.
     - Scan through the array in pairs. The element that does not match its pair is the answer.

     Approach Steps:
     1. Sort the array.
     2. Traverse from index 1 to n-1 in steps of 2.
     3. If nums[i] != nums[i-1], then nums[i-1] is the single number.
     4. If no mismatch found, the last element must be the unique one.

     Time Complexity: O(n log n) — sorting dominates.
     Space Complexity: O(1) — if sorting in-place.
    */
    private static int singleNumberBetter1(int[] nums) {
        if(nums.length == 1)
            return nums[0];

        Arrays.sort(nums);

        for(int i=1; i<nums.length; i+=2){
            if(nums[i] != nums[i-1]){
                return nums[i-1];
            }
        }

        return nums[nums.length - 1];
    }

    /*
     Better Approach #2 (HashMap Frequency Count):
     - Count the frequency of each element using a HashMap.
     - Return the element with frequency 1.

     Approach Steps:
     1. Create a frequency map.
     2. Traverse nums and increment count for each element.
     3. Loop through map entries to find the key with value 1.

     Time Complexity: O(n) — single pass to build map, another pass to inspect values.
     Space Complexity: O(n) — frequency map holds up to n distinct elements.
    */
    private static int singleNumberBetter2(int[] nums) {
        if(nums.length == 1)
            return nums[0];

        HashMap<Integer, Integer> freqMap = new HashMap<>();
        for(int val : nums){
            freqMap.put(val, freqMap.getOrDefault(val, 0) +1);
        }

        for(int key : freqMap.keySet()){
            if(freqMap.get(key) == 1){
                return key;
            }
        }

        return -1;
    }

    /*
      Optimized Approach (XOR Trick):
      Key Insight:
      - XOR has special properties that make it perfect for this problem:
        1. a ^ a = 0            → duplicates cancel out
        2. a ^ 0 = a            → XOR with zero keeps the number unchanged
        3. XOR is commutative & associative → order doesn't matter

      Explanation:
      - If every number except one appears twice, XORing all elements together will cancel all pairs.
      - The remaining value will be the element that appears once.

      Approach Steps:
      1. Initialize `ans = 0`.
      2. For every number `val` in nums, compute ans ^= val.
      3. After processing all numbers, ans will contain the unique element.

      Time Complexity: O(n) — single pass.
      Space Complexity: O(1) — constant extra space.
     */
    private static int singleNumberOptimized(int[] nums) {
        if(nums.length == 1)
            return nums[0];

        int ans = 0;
        for(int val : nums){
            ans ^= val;
        }

        return ans;
    }


}
