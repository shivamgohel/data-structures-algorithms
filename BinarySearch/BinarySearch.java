// https://leetcode.com/problems/binary-search/description/

import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given a sorted array of integers `nums` and an integer `target`,
         return the index of `target` if it exists in the array.
         If `target` is not present, return -1.

         Constraints:
         - Array is sorted in ascending order

         Example:
         Input: nums = [-1,0,3,5,9,12], target = 9
         Output: 4

         Explanation:
         - 9 exists at index 4 in the array
        */

        int[] nums = {-1,0,3,5,9,12};
        int target = 9;

        // Brute-force approach → TC: O(N), SC: O(1)
        System.out.println(searchBrute(nums, target));

        // Optimized binary search → TC: O(log N), SC: O(1)
        System.out.println(searchOptimized(nums, target));

        // Built-in binary search → TC: O(log N), SC: O(1)
        System.out.println(searchBuiltIn(nums, target));
    }

    /*
     Approach 1: Brute-force (Linear Search)
     --------------------------------------
     Problem:
     - Search for a target element in a sorted array.
     - Even though the array is sorted, we ignore that fact in this approach.

     Intuition / Why Brute-force:
     -----------------------------
     - The simplest possible solution.
     - Iterate through every element until the target is found.
     - Easy to implement but inefficient for large arrays.

     Step-by-Step Thought Process:
     -----------------------------
     1. Start from index 0.
     2. Compare each element with the target.
     3. If a match is found, return the index.
     4. If the loop ends without finding the target, return -1.

     Example Walkthrough:
     --------------------
     nums = [-1, 0, 3, 5, 9, 12], target = 9

     - Compare -1 → no
     - Compare 0  → no
     - Compare 3  → no
     - Compare 5  → no
     - Compare 9  → yes → return index 4

     Complexity Analysis:
     --------------------
     - Time: O(N)
     - Space: O(1)
    */
    private static int searchBrute(int[] nums, int target){
        int res = -1;
        for(int i=0; i<nums.length; i++){
            if(nums[i] == target){
                res = i;
                break;
            }
        }

        return res;
    }

    /*
     Approach 2: Binary Search (Optimized)
     ------------------------------------
     Problem:
     - The array is sorted → we can eliminate half the search space at each step.

     Intuition / Why Binary Search:
     -------------------------------
     - Instead of checking every element, repeatedly divide the array in half.
     - Compare the middle element with the target:
       - If equal → target found
       - If target is smaller → search left half
       - If target is larger → search right half

     Step-by-Step Thought Process:
     -----------------------------
     1. Initialize two pointers:
        - `low` = start of the array
        - `high` = end of the array
     2. While `low <= high`:
        a) Compute middle index:
           mid = low + (high - low) / 2
        b) Compare nums[mid] with target:
           - If equal → return mid
           - If nums[mid] < target → move `low` right
           - Else → move `high` left
     3. If the loop ends → target not found → return -1

     Example Walkthrough:
     --------------------
     nums = [-1, 0, 3, 5, 9, 12], target = 9

     - low=0, high=5 → mid=2 → nums[2]=3 < 9 → search right
     - low=3, high=5 → mid=4 → nums[4]=9 → found → return 4

     Complexity Analysis:
     --------------------
     - Time: O(log N)
     - Space: O(1)
    */
    private static int searchOptimized(int[] nums, int target){
        int low = 0;
        int high = nums.length-1;

        while(low <= high){
            int mid = low + (high-low)/2;

            if(nums[mid] == target)
                return mid;
            else if(nums[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    /*
     Approach 3: Built-in Binary Search (Java Library)
     -------------------------------------------------
     Problem:
     - Java provides a built-in binary search method for sorted arrays.

     Intuition / Why Built-in:
     --------------------------
     - Saves implementation time.
     - Internally uses binary search.
     - Useful in real-world applications, but not preferred in interviews.

     Important Detail:
     -----------------
     Arrays.binarySearch():
     - Returns index ≥ 0 → target found
     - Returns negative value → target not found
       (specifically: -insertionPoint - 1)

     Step-by-Step Thought Process:
     -----------------------------
     1. Call Arrays.binarySearch(nums, target).
     2. If return value ≥ 0 → return index.

     Complexity Analysis:
     --------------------
     - Time: O(log N)
     - Space: O(1)
    */
    private static int searchBuiltIn(int[] nums, int target){
        // int idx = Collections.binarySearch(nums,target);

        int idx = Arrays.binarySearch(nums, target);

        return idx >= 0 ? idx : -1;
    }

}
