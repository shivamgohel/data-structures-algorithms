// https://leetcode.com/problems/kth-largest-element-in-an-array/description/

import java.util.Arrays;
import java.util.PriorityQueue;

public class KthLargestElementInAnArray {
    public static void main(String[] args) {

        /*
          Problem Statement:
          -----------------
          Given an integer array `nums` and an integer `k`, return the kth largest element in the array.
          Note that it is the kth largest element in sorted order, not the kth distinct element.

          Example:
          --------
          Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
          Output: 4
          Explanation: The sorted array is [1, 2, 2, 3, 3, 4, 5, 5, 6].
          The 4th largest element is 4.
        */

        int[] nums = {3,2,3,1,2,4,5,5,6};
        int k = 4;

        // Approach 1: Brute-force -> Ascending Sort -> TC: O(n log n), SC: O(1)
        System.out.println(findKthLargestBrute1(nums, k));

        // Approach 2: Brute-force -> Descending Sort {Sort and Reverse} -> TC: O(n log n), SC: O(1)
        System.out.println(findKthLargestBrute2(nums, k));

        // Approach 3: Min-Heap (Optimized) -> TC: O(n log k), SC: O(k)
        System.out.println(findKthLargestOptimized(nums, k));

    }

    /*
      Approach 1: Brute-force (Sort and Access)
      -----------------------------------------
      Intuition:
      - Sorting the array in ascending order puts the largest elements at the end.
      - The kth largest element will be at index (length - k).

      Complexity Analysis:
      --------------------
      - Time: O(n log n) -> Sorting the primitive array.
      - Space: O(1) -> Sorting in-place.
    */
    private static int findKthLargestBrute1(int[] nums, int k){
        Arrays.sort(nums);

        return nums[nums.length - k];
    }

    /*
      Approach 2: Brute-force (Sort and Manual Reverse)
      -------------------------------------------------
      Intuition:
      - Since Arrays.sort() doesn't support custom comparators for primitives,
        we sort ascending and then manually reverse the array to get descending order.
      - The kth largest is now at index (k - 1).

      Complexity Analysis:
      --------------------
      - Time: O(n log n) -> Sort + O(n) reversal.
      - Space: O(1)
    */
    private static int findKthLargestBrute2(int[] nums, int k){
        // Arrays.sort() cannot use a custom Comparator with primitive types like int[]
        // (convert it into Integer[] then use custom comparator) or (sort and reverse).

        Arrays.sort(nums);
        for(int i=0; i<nums.length/2; i++){
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - i];
            nums[nums.length - 1 - i] = temp;
        }

        return nums[k - 1];
    }

    /*
      Approach 3: Min-Heap (Optimized for K)
      -------------------------------------
      Problem:
      - Sorting the entire array is wasteful if k is small and n is huge.

      Intuition:
      - We maintain a Min-Heap of size k.
      - A Min-Heap keeps the smallest element at the top.
      - By keeping the heap size at k, we effectively keep the "k largest elements"
        seen so far. The smallest among these k largest elements (at the root)
        is our kth largest element.

      Dry Run (Example: nums = [3,2,3,1,2,4,5,5,6], k = 4):
      ---------------------------------------------------------
      1. Process 3: Heap [3]
      2. Process 2: Heap [2, 3]
      3. Process 3: Heap [2, 3, 3]
      4. Process 1: Heap [1, 2, 3, 3]
      5. Process 2: Heap [1, 2, 2, 3, 3] -> Size > 4, poll(1). Heap [2, 2, 3, 3]
      6. Process 4: Heap [2, 2, 3, 3, 4] -> Size > 4, poll(2). Heap [2, 3, 3, 4]
      7. Process 5: Heap [2, 3, 3, 4, 5] -> Size > 4, poll(2). Heap [3, 3, 4, 5]
      8. Process 5: Heap [3, 3, 4, 5, 5] -> Size > 4, poll(3). Heap [3, 4, 5, 5]
      9. Process 6: Heap [3, 4, 5, 5, 6] -> Size > 4, poll(3). Heap [4, 5, 5, 6]

      Final result: minHeap.peek() is 4. (The root of the k-sized heap).

      Step-by-Step Thought Process:
      -----------------------------
      1. Initialize a Min-Heap (Default PriorityQueue in Java).
      2. Iterate through all numbers in the array:
         a. Add the number to the heap.
         b. If heap size exceeds k, poll() the smallest element.
      3. After the loop, the heap root is the kth largest element.

      Complexity Analysis:
      --------------------
      - Time: O(n log k) -> We process n elements, each heap operation is log k.
      - Space: O(k) -> To store k elements in the heap.
    */
    private static int findKthLargestOptimized(int[] nums, int k){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for(int x : nums){
            minHeap.offer(x);

            if(minHeap.size() > k)
                minHeap.poll();
        }

        return minHeap.peek();
    }

}
