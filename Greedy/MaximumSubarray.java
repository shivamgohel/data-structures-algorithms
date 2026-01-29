// https://leetcode.com/problems/maximum-subarray/description/
import java.util.*;

class MaximumSubarray
{
    public static void main(String[] args) 
    {
        /*
         Problem Statement:
         Given an integer array `nums`, find the contiguous subarray (containing at least one number) 
         which has the largest sum and return its sum.
        */

        int nums[] = {2,-3,4,-2,2,1,-1,4};

        // Brute-force approach: O(n^2) time, O(1) space
        System.out.println(maximumSubarrayBrute(nums));

        // Better approach (using DP array): O(n) time, O(n) space
        System.out.println(maximumSubarrayBetter(nums));

        // Optimized approach (Kadane’s Algorithm): O(n) time, O(1) space
        System.out.println(maximumSubarrayOptimized1(nums));

        // Optimized approach (Kadane’s Algorithm  { Handles negative numbers elegantly }): O(n) time, O(1) space
        System.out.println(maximumSubarrayOptimized2(nums));



        // Follow-up: Return the actual subarray with maximum sum
        System.out.println(Arrays.toString(maximumSubarrayFollowUp(nums)));



    }

    /*
     Brute-force Approach:
     - This approach checks all possible subarrays and calculates their sums.
     - It updates the maximum sum found across all subarrays.

     Approach Steps:
     1. Use two nested loops:
        - Outer loop `i` sets the start index.
        - Inner loop `j` adds up elements from `i` to `j`.
     2. Update `maximumSum` whenever a larger sum is found.
     3. Return `maximumSum` after checking all subarrays.

     Time Complexity: O(n^2) — two nested loops over array elements.
     Space Complexity: O(1) — only variables are used.
    */
    private static int maximumSubarrayBrute(int[] nums) {
        if(nums.length == 0)
            return -1;
        
        int maximumSum = Integer.MIN_VALUE;

        for(int i=0;i<nums.length;i++)
        {
            int currentSum = 0;
            for(int j=i;j<nums.length;j++)
            {
                currentSum += nums[j];
                maximumSum = Math.max(maximumSum, currentSum);
            }
        } 

        return maximumSum;
    }

    /*
     Better Approach: Dynamic Programming
     - We use a `dp` array where `dp[i]` represents the maximum subarray sum ending at index `i`.
     - At each index, we decide:
        - Either start a new subarray from current element.
        - Or extend the previous subarray.

     Approach Steps:
     1. Initialize `dp[0]` to `nums[0]`.
     2. For each index `i`, set:
        - dp[i] = max(nums[i], nums[i] + dp[i-1])
     3. After filling `dp` array, the maximum value in `dp` is the answer.

     Time Complexity: O(n) — single pass through array.
     Space Complexity: O(n) — extra `dp` array of size n.
    */
    private static int maximumSubarrayBetter(int[] nums) {
        if(nums.length == 0)
            return -1;

        int maximumSum = Integer.MIN_VALUE;
        
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for(int i=1;i<nums.length;i++)
            dp[i] = Math.max(nums[i], nums[i] + dp[i-1]);

        for(int i=0;i<nums.length;i++)
            maximumSum = Math.max(maximumSum, dp[i]);

        return maximumSum;        
    }

    /*
     Optimized Approach: Kadane's Algorithm
     - Instead of using extra space, we optimize the better approach.
     - We track the running sum and reset it to 0 if it becomes negative.
     - Always keep track of the maximum sum seen so far.

     Approach Steps:
     1. Initialize `currentSum` to 0 and `maximumSum` to minimum value.
     2. For each element:
        - Add it to `currentSum`.
        - Update `maximumSum` if `currentSum` is larger.
        - If `currentSum` becomes negative, reset it to 0.

     Time Complexity: O(n) — single pass.
     Space Complexity: O(1) — no extra space.
    */
    private static int maximumSubarrayOptimized1(int[] nums) {
        if(nums.length == 0)
            return -1;
        
        int maximumSum = Integer.MIN_VALUE;
        int currentSum = 0;

        for(int num : nums)
        {
            currentSum += num;
            maximumSum = Math.max(maximumSum, currentSum);
            currentSum = Math.max(currentSum, 0);
        }    

        return maximumSum;
    }

    /*
    Optimized Approach 2: Kadane's Algorithm (currSum = max(currSum + num, num))
    ---------------------------------------------------------------------------
    Why this is better than Optimized Approach 1 :

    1. Handles negative numbers elegantly:
       - At each step, we choose between:
           a) Extending the previous subarray sum: currSum + num
           b) Starting a new subarray from the current element: num
       - This ensures that when the previous sum is negative, we automatically "discard" it
         and start fresh from the current number.
       - Unlike approach1 (simple reset to 0), this also works correctly if
         all numbers are negative (it picks the largest single number).

    2. Simpler logic:
       - No need to check if currSum < 0 and reset separately.
       - Single line: currSum = Math.max(currSum + num, num)
       - maxSum = Math.max(maxSum, currSum) keeps track of the largest sum seen.

    3. Time and Space efficiency:
       - Time Complexity: O(n) — single pass through the array.
       - Space Complexity: O(1) — no extra array or storage required.

    Example Dry Run (nums = [2, -3, 4, -2, 2, 1, -1, 4]):
    ---------------------------------------------------------------------------
    Step-by-step currSum and maxSum updates:
    Index 0: num=2    → currSum = max(0+2, 2) = 2   → maxSum = 2
    Index 1: num=-3   → currSum = max(2-3, -3) = -1 → maxSum = 2
    Index 2: num=4    → currSum = max(-1+4, 4) = 4  → maxSum = 4
    Index 3: num=-2   → currSum = max(4-2, -2) = 2  → maxSum = 4
    Index 4: num=2    → currSum = max(2+2, 2) = 4   → maxSum = 4
    Index 5: num=1    → currSum = max(4+1, 1) = 5   → maxSum = 5
    Index 6: num=-1   → currSum = max(5-1, -1) = 4  → maxSum = 5
    Index 7: num=4    → currSum = max(4+4, 4) = 8   → maxSum = 8

    Final maximum sum = 8

    Key Advantage:
    - Automatically discards negative contributions.
    - Correctly handles arrays with all negative numbers.
    - More concise and robust than resetting currSum manually.
    */
    private static int maximumSubarrayOptimized2(int[] nums){
        int currSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for(int num : nums){
            currSum = Math.max(currSum+num, num);
            maxSum = Math.max(maxSum, currSum);
        }

        return maxSum;
    }

    /*
     Follow-up: Return the actual subarray having maximum sum
     - In addition to tracking maximum sum, we also track the start and end indices.
     - Reset start whenever `currentSum` becomes negative.

     Approach Steps:
     1. Maintain variables:
        - `start` (temporary start)
        - `ansStart` and `ansEnd` (for final answer)
     2. Update `ansStart` and `ansEnd` whenever a new maximum is found.
     3. Return the subarray using these indices.

     Time Complexity: O(n) — single pass.
     Space Complexity: O(k) — size of result subarray.
    */
    private static int[] maximumSubarrayFollowUp(int[] nums) {

        int maximumSum = Integer.MIN_VALUE;
        int currentSum = 0;

        int start = 0;
        int ansStart = 0;
        int ansEnd = 0;    

        for(int i=0;i<nums.length;i++)
        {
            currentSum += nums[i];

            if(currentSum > maximumSum)
            {
                maximumSum = currentSum;
                ansStart = start;
                ansEnd = i;
            }

            if(currentSum < 0)
            {
                currentSum = 0;
                start = i + 1;
            }
        }

        int[] result = new int[ansEnd - ansStart + 1];
        for(int i=ansStart; i<= ansEnd; i++)
        {
            result[i-ansStart] = nums[i];
        }

        return result;
    }
}