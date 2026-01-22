// https://leetcode.com/problems/search-in-rotated-sorted-array/description/

public class SearchinRotatedSortedArray {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given a rotated sorted array `nums` (no duplicates) and a `target` value,
         find the index of `target` in the array. If it does not exist, return -1.

         Example:
         --------
         Input: nums = [4,5,6,7,0,1,2], target = 0
         Output: 4
         Explanation:
         - Array is rotated at pivot index 4.
         - Target value 0 is located at index 4.
        */

        int[] nums = {4,5,6,7,0,1,2};
        int target = 0;

        // Approach 1: Brute-force → scan entire array → TC: O(n), SC: O(1)
        System.out.println(searchBrute(nums, target));

        // Approach 2: Optimized Binary Search → O(log n) → TC: O(log n), SC: O(1)
        System.out.println(searchOptimized(nums, target));
    }

    /*
     Approach 1: Brute-force
     -----------------------
     Problem:
     - Array may be rotated, target may exist anywhere.

     Intuition:
     - The simplest way is to scan all elements linearly and check for target.

     Step-by-Step Thought Process:
     -----------------------------
     1. Loop through the array from index 0 to nums.length-1
     2. If nums[i] == target → return i
     3. If loop ends without finding target → return -1

     Complexity Analysis:
     --------------------
     - Time: O(n) → check each element once
     - Space: O(1) → constant extra space
    */
    private static int searchBrute(int[] nums, int target){
        for(int i=0; i<nums.length; i++){
            if(nums[i] == target)
                return i;
        }

        return -1;
    }

    /*
     Approach 2: Optimized Binary Search
     ----------------------------------
     Problem:
     - Array is rotated sorted → cannot use normal binary search directly.
     - Goal: O(log n) time complexity.

     Intuition:
     -----------
     1. In a rotated sorted array, **one half is always sorted**.
     2. We can check which half is sorted by comparing nums[low] and nums[mid].
     3. Once sorted half is identified:
        - If target lies in this half → search there
        - Else → search in the other half

     Step-by-Step Thought Process:
     -----------------------------
     1. Initialize two pointers:
        - low = 0, high = nums.length - 1
     2. While low <= high:
        a. Compute mid = low + (high - low)/2
        b. Check if nums[mid] == target → return mid
        c. Check if left half is sorted: nums[low] <= nums[mid]
           - If target lies between nums[low] and nums[mid]:
               → high = mid - 1 (search left half)
           - Else:
               → low = mid + 1 (search right half)
        d. Else (right half must be sorted):
           - If target lies between nums[mid] and nums[high]:
               → low = mid + 1 (search right half)
           - Else:
               → high = mid - 1 (search left half)
     3. If loop ends without finding target → return -1

     Dry Run Example:
     ----------------
     nums = [4,5,6,7,0,1,2], target = 0
     - Step 1: low=0, high=6, mid=3 → nums[mid]=7
       - left half sorted (4 to 7)
       - target=0 not in left half → search right half → low=4
     - Step 2: low=4, high=6, mid=5 → nums[mid]=1
       - left half sorted (0 to 1)
       - target=0 in left half → high=mid-1=4
     - Step 3: low=4, high=4, mid=4 → nums[mid]=0 → found → return 4

     Complexity Analysis:
     --------------------
     - Time: O(log n) → each iteration halves the search space
     - Space: O(1) → constant extra space
    */
    private static int searchOptimized(int[] nums, int target){
        int low = 0;
        int high = nums.length-1;

        while(low <= high){
            int mid = low + (high-low)/2;

            if(nums[mid] == target)
                return mid;

            // left half is sorted
            if(nums[low] <= nums[mid]){
                if(nums[low] <= target && target <= nums[mid]){ // { element is in left half  }
                    high = mid - 1;
                }else{                                          // { element is in right half }
                    low = mid + 1;
                }
            }
            // right half is sorted
            else{
                if(nums[mid] <= target && target <= nums[high]){ // { element is in right half }
                    low = mid + 1;
                }else{                                           //  { element is in left half }
                    high = mid - 1;
                }
            }
        }

        return -1;
    }

}
