// https://leetcode.com/problems/median-of-two-sorted-arrays/description/

public class MedianofTwoSortedArrays {

    public static void main(String[] args) {

        /*
         Problem Statement:
         ------------------
         Given two sorted arrays nums1 and nums2 of size m and n respectively,
         return the **median** of the two sorted arrays.

         The overall run time complexity should be O(log (m+n)).

         Example:
         --------
         nums1 = [1,2]
         nums2 = [3,4]

         Combined sorted array = [1,2,3,4]
         Median = (2 + 3) / 2 = 2.5
        */

        int[] nums1 = {1,2};
        int[] nums2 = {3,4};

        // Approach 1: Brute Force (Merge arrays) → TC: O(m + n), SC: O(m + n)
        System.out.println(findMedianSortedArraysBrute(nums1, nums2));

        // Approach 2: Better (Merge without extra array) → TC: O(m + n), SC: O(1)
        System.out.println(findMedianSortedArraysBetter(nums1, nums2));

        // Approach 3: Optimized (Binary Search on partitions) → TC: O(log(min (m, n) )), SC: O(1)
        System.out.println(findMedianSortedArraysOptimized(nums1, nums2));


    }

    /*
     Approach 1: Brute Force (Merge Two Sorted Arrays)
     -------------------------------------------------
     Problem:
     --------
     - We are given two individually sorted arrays nums1 and nums2.
     - We need to find the median of the combined sorted array.

     Intuition / Why Brute Force:
     -----------------------------
     - Since both arrays are already sorted, the most straightforward
       solution is to **merge them into a single sorted array**.
     - Once merged, finding the median becomes trivial because:
         • If total length is odd → median is the middle element
         • If total length is even → median is the average of two middle elements
     - This approach closely resembles the merge step of Merge Sort.

     Step-by-Step Thought Process:
     -----------------------------
     1. Calculate total length:
        - totalLength = nums1.length + nums2.length
        - This determines the median position(s).

     2. Create a new array to store merged result:
        - int[] res = new int[totalLength]
        - This array will contain all elements in sorted order.

     3. Use two pointers to merge arrays:
        - left  → pointer for nums1
        - right → pointer for nums2
        - idx   → pointer for merged array

        - Compare nums1[left] and nums2[right]
        - Insert the smaller value into res[idx]
        - Move the corresponding pointer forward

     4. Copy remaining elements:
        - If one array is exhausted, copy all remaining elements
          from the other array directly into res.

     5. Find the median:
        - If totalLength is even:
            median = (res[totalLength/2 - 1] + res[totalLength/2]) / 2.0
        - If totalLength is odd:
            median = res[totalLength/2]

     Why this works:
     ----------------
     - Merging guarantees the combined array is fully sorted.
     - Median is always determined by the middle element(s) of a sorted list.
     - No special edge handling is required once merge is correct.

     Example Dry Run:
     ----------------
     nums1 = [1, 2]
     nums2 = [3, 4]

     Merged array:
       res = [1, 2, 3, 4]

     totalLength = 4 (even)
     median = (res[1] + res[2]) / 2
            = (2 + 3) / 2
            = 2.5

     Complexity Analysis:
     --------------------
     - Time Complexity: O(m + n)
       • Every element from both arrays is visited exactly once.

     - Space Complexity: O(m + n)
       • Extra array used to store merged result.
    */
    private static double findMedianSortedArraysBrute(int[] nums1, int[] nums2){
        int totalLength = nums1.length + nums2.length;

        int[] res = new int[totalLength];
        int idx = 0;                    // pointer for res array
        int left = 0;                   // pointer for nums1 array
        int right = 0;                  // pointer for nums2 array

        while(left < nums1.length && right < nums2.length){
            if(nums1[left] <= nums2[right])
                res[idx++] = nums1[left++];
            else
                res[idx++] = nums2[right++];
        }
        while(left < nums1.length)
            res[idx++] = nums1[left++];
        while(right < nums2.length)
            res[idx++] = nums2[right++];

        //
        if(totalLength % 2 == 0){
            return (res[totalLength/2 - 1] + res[totalLength/2]) / 2.0;
        }

        return res[totalLength/2];
    }

    /*
     Approach 2: Better (Merge Without Extra Array)
     ----------------------------------------------
     Problem:
     --------
     - We are given two sorted arrays nums1 and nums2.
     - We need to find the median of the combined sorted order.

     Intuition / Why This Is Better:
     -------------------------------
     - In the brute-force approach, we merged both arrays into a new array,
       which required O(m + n) extra space.
     - Observing the problem, we realize that **we don’t need the full merged array**.
     - We only care about the **middle element(s)** that form the median.
     - By simulating the merge process and tracking indices, we can extract
       only the required median values.

     Key Observation:
     ----------------
     - Median positions depend on total length:
         • If total length is even:
             firstMedianIndex  = (totalLength / 2) - 1
             secondMedianIndex = (totalLength / 2)
         • If total length is odd:
             medianIndex = totalLength / 2
     - While merging, we keep a virtual index to know when we reach these positions.

     Step-by-Step Thought Process:
     -----------------------------
     1. Calculate total length:
        - totalLength = nums1.length + nums2.length

     2. Identify median indices:
        - secondMedianIndex = totalLength / 2
        - firstMedianIndex  = secondMedianIndex - 1

     3. Initialize pointers:
        - left  → pointer for nums1
        - right → pointer for nums2
        - currentIndex → index in the virtual merged array

     4. Traverse both arrays in sorted order:
        - At each step, pick the smaller element between nums1[left] and nums2[right].
        - Before moving forward, check:
            • If currentIndex == firstMedianIndex → store firstMedianValue
            • If currentIndex == secondMedianIndex → store secondMedianValue
        - Increment currentIndex and the corresponding pointer.

     5. Handle remaining elements:
        - If one array finishes early, continue traversing the other array.
        - Continue checking for median indices during traversal.

     6. Compute median:
        - If total length is even:
            median = (firstMedianValue + secondMedianValue) / 2.0
        - If total length is odd:
            median = secondMedianValue

     Why This Works:
     ---------------
     - The merge process guarantees sorted order without explicitly storing it.
     - The median depends only on middle elements, not the full array.
     - Tracking indices allows us to stop caring about elements once median is found.

     Example Dry Run:
     ----------------
     nums1 = [1, 2]
     nums2 = [3, 4]

     Virtual merged order: [1, 2, 3, 4]
     firstMedianIndex  = 1
     secondMedianIndex = 2

     Values picked during traversal:
       index 1 → 2
       index 2 → 3

     median = (2 + 3) / 2 = 2.5

     Complexity Analysis:
     --------------------
     - Time Complexity: O(m + n)
       • Each element is visited at most once.

     - Space Complexity: O(1)
       • No extra array is used; only variables.
    */
    private static double findMedianSortedArraysBetter(int[] nums1, int[] nums2){
        int totalLength = nums1.length + nums2.length;

        int secondMedianIndex = totalLength/2;
        int firstMedianIndex = secondMedianIndex - 1;
        int firstMedianValue = -1;
        int secondMedianValue = -1;

        int left = 0;                   // pointer for nums1 array
        int right = 0;                  // pointer for nums1 array
        int currentIndex = 0;           // pointer for extracting firstMedianValue & secondMedianValue

        while(left < nums1.length && right < nums2.length){
            if(nums1[left] <= nums2[right]){
                if(currentIndex == firstMedianIndex) firstMedianValue = nums1[left];
                if(currentIndex == secondMedianIndex) secondMedianValue = nums1[left];

                currentIndex++;
                left++;
            }
            else{
                if(currentIndex == firstMedianIndex) firstMedianValue = nums2[right];
                if(currentIndex == secondMedianIndex) secondMedianValue = nums2[right];

                currentIndex++;
                right++;
            }
        }

        while(left < nums1.length){
            if(currentIndex == firstMedianIndex) firstMedianValue = nums1[left];
            if(currentIndex == secondMedianIndex) secondMedianValue = nums1[left];

            currentIndex++;
            left++;
        }

        while(right < nums2.length){
            if(currentIndex == firstMedianIndex) firstMedianValue = nums2[right];
            if(currentIndex == secondMedianIndex) secondMedianValue = nums2[right];

            currentIndex++;
            right++;
        }

        if(totalLength % 2 == 0){
            return (firstMedianValue + secondMedianValue) / 2.0;
        }

        return secondMedianValue;
    }

    /*
     Approach 3: Optimized (Binary Search on Partitions)
     ---------------------------------------------------
     Problem:
     --------
     - We are given two sorted arrays nums1 and nums2.
     - We need to find the median of the combined sorted order.
     - The required time complexity is O(log(m + n)).

     Intuition / Why Binary Search Works:
     -----------------------------------
     - Instead of merging arrays, we divide both arrays into two partitions
       such that:
         • Left partition contains exactly half of the total elements
         • Right partition contains the remaining elements
       and
         • Every element in the left partition ≤ every element in the right partition
     - Since arrays are sorted, partition validity can be checked in O(1).
     - Binary search efficiently finds this valid partition.

     Key Observations:
     -----------------
     - Binary search is always applied on the **smaller array**.
     - Let:
         n1 = nums1.length
         n2 = nums2.length
     - Elements in left partition:
         (n1 + n2 + 1) / 2
       (+1 handles odd total length correctly)

     Partition Definition:
     ---------------------
     - mid1 → number of elements taken from nums1
     - mid2 → number of elements taken from nums2

       mid2 = (n1 + n2 + 1) / 2 - mid1

     Partition Boundary Elements:
     -----------------------------
     - left1  = element just left of partition in nums1
     - right1 = element just right of partition in nums1
     - left2  = element just left of partition in nums2
     - right2 = element just right of partition in nums2

     Edge Case Handling:
     -------------------
     - If partition is at the start of an array → Integer.MIN_VALUE
     - If partition is at the end of an array   → Integer.MAX_VALUE
     - This avoids bounds checking and simplifies comparisons.

     Valid Partition Condition:
     ---------------------------
     - left1 <= right2 AND left2 <= right1

     Median Calculation:
     -------------------
     - If total length is even:
         median = (max(left1, left2) + min(right1, right2)) / 2.0
     - If total length is odd:
         median = max(left1, left2)

     Binary Search Movement:
     -----------------------
     - If left1 > right2:
         → too many elements taken from nums1
         → move left → high = mid1 - 1
     - Else (left2 > right1):
         → not enough elements taken from nums1
         → move right → low = mid1 + 1

     ---------------------------------------------------
     Example Dry Run (Detailed with Pointers & Partitions)
     ---------------------------------------------------
     nums1 = [1, 4, 7]        // size = 3
     nums2 = [2, 3, 5, 6]     // size = 4

     n1 = 3, n2 = 4
     totalLength = 7 (odd)

     Left partition size:
       leftSize = (n1 + n2 + 1) / 2
                = (3 + 4 + 1) / 2
                = 4

     Binary Search Range:
       low = 0, high = 3

     --------------------
     Iteration 1:
     --------------------
     mid1 = (0 + 3) / 2 = 1
     mid2 = leftSize - mid1 = 4 - 1 = 3

     Partitions:
       nums1: [ 1 | 4, 7 ]
                ↑ mid1

       nums2: [ 2, 3, 5 | 6 ]
                       ↑ mid2

     Boundary Elements:
       left1  = nums1[0] = 1
       right1 = nums1[1] = 4
       left2  = nums2[2] = 5
       right2 = nums2[3] = 6

     Check:
       left1 <= right2 → 1 <= 6 ✔
       left2 <= right1 → 5 <= 4 ✘

     Action:
       - left2 > right1
       - Not enough elements from nums1
       - Move right → low = mid1 + 1 = 2

     --------------------
     Iteration 2:
     --------------------
     mid1 = (2 + 3) / 2 = 2
     mid2 = leftSize - mid1 = 4 - 2 = 2

     Partitions:
       nums1: [ 1, 4 | 7 ]
                     ↑ mid1

       nums2: [ 2, 3 | 5, 6 ]
                     ↑ mid2

     Boundary Elements:
       left1  = nums1[1] = 4
       right1 = nums1[2] = 7
       left2  = nums2[1] = 3
       right2 = nums2[2] = 5

     Check:
       left1 <= right2 → 4 <= 5 ✔
       left2 <= right1 → 3 <= 7 ✔

    --> Valid partition found

     Median:
     -------
     totalLength is odd
     median = max(left1, left2)
            = max(4, 3)
            = 4

     ---------------------------------------------------
     Why This Works:
     ---------------------------------------------------
     - No array merging is required.
     - Binary search reduces the search space each step.
     - Correct partition guarantees correct median.

     Complexity Analysis:
     --------------------
     - Time Complexity: O(log(min(m, n)))
     - Space Complexity: O(1)
    */
    private static double findMedianSortedArraysOptimized(int[] nums1, int[] nums2){
        if(nums1.length > nums2.length)
            return findMedianSortedArraysOptimized(nums2, nums1);

        int totalLength = nums1.length + nums2.length;
        int n1 = nums1.length;
        int n2 = nums2.length;

        int low = 0;
        int high = n1;

        while(low <= high){
            int mid1 = (low+high)/2;                                       // Number of elements taken from nums1 for the left partition
            int mid2 = (n1 + n2 + 1)/2 - mid1;                             // Number of elements taken from nums2 for the left partition

            int left1 = (mid1 == 0) ? Integer.MIN_VALUE : nums1[mid1-1];   // Last element in nums1’s left partition
            int left2 = (mid2 == 0) ? Integer.MIN_VALUE : nums2[mid2-1];   // Last element in nums2’s left partition
            int right1 = (mid1 == n1) ? Integer.MAX_VALUE : nums1[mid1];   // First element in nums1’s right partition
            int right2 = (mid2 == n2) ? Integer.MAX_VALUE : nums2[mid2];   // First element in nums2’s right partition

            if(left1 <= right2 && left2 <= right1){
                if(totalLength % 2 == 0)
                    return (Math.max(left1, left2) + Math.min(right1, right2)) / 2.0;
                else
                    return Math.max(left1, left2);
            }
            else if(left1 > right2)
                high = mid1 - 1;
            else
                low = mid1 + 1;
        }

        return 0.0;
    }

}

