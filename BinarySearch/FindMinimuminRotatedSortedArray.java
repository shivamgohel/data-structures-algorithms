// https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/description/

import java.util.Arrays;

public class FindMinimuminRotatedSortedArray {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         You are given a rotated sorted array `nums` (no duplicates).
         The array was originally sorted in ascending order, then rotated at some pivot.
         Your task is to find the **minimum element** in the array.

         Example:
         --------
         Input: nums = [3,4,5,1,2]
         Output: 1
         Explanation:
         - The array was rotated at pivot index 3.
         - The minimum element is 1.
        */

        int[] nums = {3,4,5,1,2};

        // Approach 1: Brute-force → scan entire array → TC: O(n), SC: O(1)
        System.out.println(findMinBrute1(nums));

        // Approach 2: Brute-force using Java streams → TC: O(n), SC: O(1)
        System.out.println(findMinBrute2(nums));

        // Approach 3: Binary Search → O(log n) → optimized for rotated sorted array
        System.out.println(findMinOptimized(nums));

        /*
          -- FOLLOW UP --

         Problem Statement:
         -----------------
         Given a rotated sorted array `nums` (no duplicates),
         find the **number of times the array has been rotated**.
         - This is equivalent to the **index of the minimum element**.

         Example:
         --------
         Input: nums = [3,4,5,1,2]
         Output: 3
         Explanation:
         - Array was originally [1,2,3,4,5], rotated 3 times.
         - Minimum element = 1, located at index 3.
        */

        // Approach 1: Brute-force → scan entire array → TC: O(n), SC: O(1)
        findMinFollowUpBrute(nums);

        // Approach 2: Binary Search → O(log n) → optimized for rotated sorted array
        findMinFollowUpOptimized(nums);

    }

    /*
     Approach 1: Brute-force
     -----------------------
     Problem:
     - Array is rotated, we want the minimum element.

     Intuition / Why Brute-force:
     -----------------------------
     - The simplest way is to **scan all elements** and keep track of the smallest.
     - No assumptions about rotation or pivot are needed.

     Step-by-Step Thought Process:
     -----------------------------
     1. Initialize `min` with first element.
     2. Loop through all elements:
        - Update `min` = Math.min(min, nums[i])
     3. Return `min`

     Complexity Analysis:
     --------------------
     - Time: O(n) → need to check every element
     - Space: O(1)
    */
    private static int findMinBrute1(int[] nums){
        int min = nums[0];
        for(int num : nums)
            min = Math.min(min, num);

        return min;
    }

    /*
     Approach 2: Brute-force using Java streams
     ------------------------------------------
     Problem:
     - We want the minimum element in the array.
     - Instead of manual looping, Java 8+ streams provide a concise way.

     How it works:
     - Arrays.stream(nums) creates an IntStream from the array.
     - .min() finds the minimum value in the stream and returns OptionalInt.
     - .getAsInt() retrieves the integer value.

     Pros:
     -----
     - Very concise and readable.
     - Less boilerplate code than a traditional for-loop.
     - Good for quick implementations or small scripts.

     Cons:
     -----
     - Internally still scans all elements → O(n) time.
     - Slight overhead due to streams (not as fast as plain loop in tight loops).
     - Throws NoSuchElementException if array is empty → need extra check if nums.length == 0.

     Complexity:
     -----------
     - Time: O(n)
     - Space: O(1) (doesn’t create extra arrays)
    */
    private static int findMinBrute2(int[] nums){
        int min = Arrays.stream(nums).min().getAsInt();

        return min;
    }

    /*
     Approach 3: Binary Search (Optimized)
     -------------------------------------
     Problem:
     - Array is rotated sorted → minimum element lies at the **pivot point**.
     - Want O(log n) time.

     Intuition:
     -----------
     1. Binary search works because:
        - Array is mostly sorted, except at rotation pivot.
        - Either left half or right half is sorted.
     2. If left half is sorted:
        - Minimum must be in right half OR at left boundary.
     3. If right half is sorted:
        - Minimum must be in left half OR at mid.
     4. Repeat until search space is reduced to minimum element.

     Step-by-Step Thought Process:
     -----------------------------
     1. Initialize pointers:
        - low = 0, high = nums.length - 1
        - ans = Integer.MAX_VALUE (store current minimum)
     2. While low <= high:
        a. mid = low + (high - low)/2
        b. Check if the subarray is fully sorted (nums[low] <= nums[mid] <= nums[high]):
           - If yes → minimum is nums[low], break loop
        c. Else, check which half is sorted:
           - If left half sorted (nums[low] <= nums[mid]):
             → minimum is either nums[low] or in right half → update ans = min(ans, nums[low]), low = mid + 1
           - Else (right half sorted):
             → minimum is either nums[mid] or in left half → update ans = min(ans, nums[mid]), high = mid - 1
     3. Return ans

     Dry Run Example:
     ----------------
     nums = [3,4,5,1,2]
     low=0, high=4, ans=INF

     1. mid=2 → nums[mid]=5
        - left half sorted → ans=min(INF,3)=3, move low=mid+1=3
     2. mid=3 → nums[mid]=1
        - right half sorted → ans=min(3,1)=1, move high=mid-1=2
     3. low>high → exit loop
        - Minimum = 1

     Complexity Analysis:
     --------------------
     - Time: O(log n)
     - Space: O(1)
    */
    private static int findMinOptimized(int[] nums){
        int low = 0;
        int high = nums.length-1;
        int ans = Integer.MAX_VALUE;

        while(low <= high){
            int mid = low + (high-low)/2;

            // array is sorted completely
            if(nums[low] <=  nums[mid]  && nums[mid] <= nums[high]){
                ans = nums[low];
                break;
            }

            // left half is sorted
            if(nums[low] <= nums[mid]){
                ans = Math.min(ans, nums[low]);
                low = mid + 1;
            }
            // right half is sorted
            else{
                ans = Math.min(ans, nums[mid]);
                high = mid - 1;
            }
        }

        return ans;
    }

    /*
     Approach 1: Brute-force to find rotation count
     ---------------------------------------------
     Problem:
     - Given a sorted array that may have been rotated, find the number of times it has been rotated.
     - Key insight: In a rotated sorted array, the **minimum element is the pivot**.
       - The index of the minimum element = number of rotations.

     Why finding minIndex works:
     ---------------------------
     - Consider a sorted array (no rotation): [1, 2, 3, 4, 5]
       - Minimum element = 1
       - Index of minimum = 0 → rotation count = 0 (array not rotated)
     - Rotate the array 1 time → [5, 1, 2, 3, 4]
       - Minimum element = 1
       - Index = 1 → rotation count = 1
     - Rotate the array 3 times → [3, 4, 5, 1, 2]
       - Minimum element = 1
       - Index = 3 → rotation count = 3
     - Rotate the array 4 times → [2, 3, 4, 5, 1]
       - Minimum element = 1
       - Index = 4 → rotation count = 4

     Step-by-step brute-force logic:
     -------------------------------
     1. Initialize:
        - minValue = nums[0] → the first element
        - minIndex = 0
     2. Loop through all elements of the array:
        - If nums[i] < minValue:
            → Update minValue = nums[i]
            → Update minIndex = i
     3. After the loop, minIndex stores the **index of the minimum element**
        - This index equals the **number of rotations**
     4. Return minIndex

     Example 1: Non-rotated array
     -----------------------------
     nums = [1, 2, 3, 4, 5]
     Loop:
     - i=0: minValue=1, minIndex=0
     - i=1..4: nums[i] > minValue → no change
     Result: minIndex=0 → rotation count = 0

     Example 2: Rotated array
     -------------------------
     nums = [3, 4, 5, 1, 2]
     Loop:
     - i=0: minValue=3, minIndex=0
     - i=1: nums[1]=4 > minValue → no change
     - i=2: nums[2]=5 > minValue → no change
     - i=3: nums[3]=1 < minValue → update minValue=1, minIndex=3
     - i=4: nums[4]=2 > minValue → no change
     Result: minIndex=3 → rotation count = 3

     Complexity:
     -----------
     - Time: O(n) → we must check every element
     - Space: O(1) → only two variables used
    */
    private static int findMinFollowUpBrute(int[] nums){
        int minIndex = 0;
        int minValue = nums[0];

        for(int i=1; i<nums.length; i++){
            if(nums[i] < minValue){
                minValue = nums[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    /*
     Approach: Optimized Binary Search to find the rotation count (index of minimum element)
     ---------------------------------------------------------------------------------------
     Problem:
     - Given a rotated sorted array, find the number of times it has been rotated.
     - Key insight: The **minimum element is the pivot**.
       - Its index = rotation count.
     - This approach uses **binary search** and tracks both **minimum value and its index**.

     Why tracking minIndex works:
     ----------------------------
     - In a rotated sorted array, the **minimum element is the pivot**.
     - Its **index = number of rotations**.
     - Example rotations of [1,2,3,4,5]:
       1. Non-rotated: [1,2,3,4,5] → min=1, index=0 → rotations=0
       2. Rotated 1 time: [5,1,2,3,4] → min=1, index=1 → rotations=1
       3. Rotated 3 times: [3,4,5,1,2] → min=1, index=3 → rotations=3

     Step-by-step logic of this implementation:
     -------------------------------------------
     1. Initialize:
        - low = 0, high = nums.length - 1
        - minValue = Integer.MAX_VALUE → to keep track of smallest element found
        - minIndex = -1 → to store index of minimum element
     2. While low <= high:
        a. Calculate mid = low + (high - low)/2
        b. Check if the current subarray is fully sorted:
           - if(nums[low] <= nums[mid] && nums[mid] <= nums[high])
           - Then nums[low] is the minimum → set minIndex = low and break
        c. Otherwise, decide which half to explore:
           - Left half sorted: nums[low] <= nums[mid]
               → Minimum could be nums[low], update minValue & minIndex if smaller
               → Move to right half: low = mid + 1
           - Right half sorted: nums[low] > nums[mid]
               → Minimum could be nums[mid], update minValue & minIndex if smaller
               → Move to left half: high = mid - 1
     3. After loop, minIndex contains index of minimum element → rotation count
     4. Return minIndex

     Example 1: Non-rotated array
     -----------------------------
     nums = [1,2,3,4,5]
     - low=0, high=4, mid=2
     - nums[low]=1 <= nums[mid]=3 <= nums[high]=5 → array fully sorted
     - minIndex = low = 0 → rotation count = 0

     Example 2: Rotated array
     -------------------------
     nums = [3,4,5,1,2]
     - Step 1: low=0, high=4, mid=2 → nums[mid]=5
       - nums[low]=3 <= nums[mid]=5, but nums[mid]=5 > nums[high]=2 → not fully sorted
       - Left half sorted → nums[low]=3 < minValue → minValue=3, minIndex=0
       - Move to right half → low=mid+1=3
     - Step 2: low=3, high=4, mid=3 → nums[mid]=1
       - nums[low]=1 <= nums[mid]=1 <= nums[high]=2 → fully sorted
       - minIndex = low = 3 → rotation count = 3

     Complexity:
     -----------
     - Time: O(log n) → binary search halves search space each step
     - Space: O(1) → only variables for pointers and min tracking
    */
    private static int findMinFollowUpOptimized(int[] nums){
        int low = 0;
        int high = nums.length-1;

        int minValue = Integer.MAX_VALUE;
        int minIndex = -1;

        while(low <= high){
            int mid = low + (high-low)/2;

            // current array is fully sorted
            if(nums[low] <= nums[mid] && nums[mid] <= nums[high]){
                minIndex = low;
                break;
            }


            // left side is sorted
            if(nums[low] <= nums[mid]){
                if(nums[low] < minValue){
                    minValue = nums[low];
                    minIndex = low;
                }
                low = mid + 1;
            }
            // right side is sorted
            else{
                if(nums[mid] < minValue){
                    minValue = nums[mid];
                    minIndex = mid;
                }
                high = mid - 1;
            }
        }

        return minIndex;
    }

}
