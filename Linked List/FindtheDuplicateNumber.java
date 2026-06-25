// https://leetcode.com/problems/find-the-duplicate-number/description/


import java.util.Arrays;
import java.util.HashSet;

public class FindtheDuplicateNumber {
    public static void main(String[] args) {

        /*
          ================================================================================
          PROBLEM STATEMENT:
          Given an array of integers `nums` containing `n + 1` integers where each integer
          is in the range `[1, n]` inclusive.

          There is only one repeated number in `nums`, return this repeated number.
          You must solve the problem without modifying the array `nums` and uses only
          constant extra space.

          Constraints:
          - 1 <= n <= 10^5
          - nums.length == n + 1
          - 1 <= nums[i] <= n
          - All the integers in nums appear only once except for precisely one integer
            which appears two or more times.
          ================================================================================
         */

        int[] nums = {1,3,4,2,2};

        // Approach 1: Brute Force (Nested Loops Comparison) | TC: O(N^2), SC: O(1)
        System.out.println(findDuplicateBrute1(nums));

        // Approach 2: Brute Force (Sorting Neighbor Check) | TC: O(N log N), SC: O(1) or O(N)
        System.out.println(findDuplicateBrute2(nums));

        // Approach 3: Better (HashSet Membership Tracking) | TC: O(N), SC: O(N)
        System.out.println(findDuplicateBetter1(nums));

        // Approach 4: Better (Integer Frequency Array Lookups) | TC: O(N), SC: O(N)
        System.out.println(findDuplicateBetter2(nums));

        // Approach 5: Better (Boolean Visited Flag Array) | TC: O(N), SC: O(N)
        System.out.println(findDuplicateBetter3(nums));

        // Approach 6: Optimized (In-place Negative Value Marking) | TC: O(N), SC: O(1)
        System.out.println(findDuplicateOptimized1(nums));

        // Approach 7: Optimized (Floyd's Cycle Detection / Tortoise & Hare) | TC: O(N), SC: O(1)
        System.out.println(findDuplicateOptimized2(nums));

    }

    /*
      Approach 1: Brute Force (Nested Loops Comparison)
      -------------------------------------------------------
      Intuition:
      - Compare every element in the array with every other element placed ahead of it.
      - The first element that matches an element at a subsequent position is our duplicate.

      Key Idea:
      "Iterate through the array with an outer anchor index, then use an inner scan loop
       to check for numerical duplicates later in the collection."

      Step-by-Step:
      1. Outer Loop: Select an element `nums[i]` as the reference anchor.
      2. Inner Loop: Scan every position `j` from `i + 1` up to the end of the array.
      3. Match Verification: Check if `nums[i] == nums[j]`. If true, return `nums[i]`.

      Why it's an anti-pattern for interviews:
      - Deeply inefficient on large datasets. The nested traversal pattern causes execution
        times to explode quadratically as the inputs scale up.

      Complexity Analysis:
      - Time: O(N^2) -> Double nested loops over a length of N.
      - Space: O(1) -> Performs execution logic inside existing bounds without extra data maps.
    */
    private static int findDuplicateBrute1(int[] nums){
        for(int i=0; i<nums.length; i++){
            for(int j=i+1; j<nums.length; j++){
                if(nums[i] == nums[j])
                    return nums[i];
            }
        }

        return -1;
    }

    /*
      Approach 2: Brute Force (Sorting Neighbor Check)
      ------------------------------------------------------
      Intuition:
      - In an unsorted array, duplicates can be scattered anywhere.
      - Sorting groups identical values right next to each other, narrowing the check down to adjacent pairs.

      Key Idea:
      "Rearrange the array into ascending order so that any identical elements are forced
       into consecutive neighboring index positions."

      Step-by-Step:
      1. Sort Phase: Rearrange elements using `Arrays.sort(nums)`.
      2. Linear Scan: Loop through the array starting from index 1.
      3. Adjacency Check: Compare `nums[i]` with `nums[i-1]`. If they match, return `nums[i]`.

      Why it's a structural fallback:
      - Mutates the original array data, violating a direct problem constraint on LeetCode.

      Complexity Analysis:
      - Time: O(N log N) -> Dominated by the backend sorting algorithm.
      - Space: O(1) or O(N) -> Dependent on whether the environment's language runtime sorts in-place.
    */
    private static int findDuplicateBrute2(int[] nums){
        Arrays.sort(nums);

        for(int i=1; i<nums.length; i++){
            if(nums[i] == nums[i-1])
                return nums[i];
        }

        return -1;
    }

    /*
      Approach 3: Better (HashSet Membership Tracking)
      ------------------------------------------------------
      Intuition:
      - Avoid sorting by remembering values we have seen in a dynamic object container.
      - HashSets provide near-instant lookups to check if an incoming item has already been encountered.

      Key Idea:
      "Stream through the data sequentially, building an explicit history list via a hash tracking collection."

      Step-by-Step:
      1. Storage Setup: Initialize an empty `HashSet<Integer>`.
      2. Traversal: Loop through each value in `nums`.
      3. Presence Verification: Check if `seen.contains(val)`. If true, return `val`.
      4. Registration: If not yet seen, add the value to the `seen` set container.

      Complexity Analysis:
      - Time: O(N) -> Single linear traversal with average O(1) hashing operations.
      - Space: O(N) -> In the worst-case scenario, the set grows to contain N unique entries.
    */
    private static int findDuplicateBetter1(int[] nums){
        HashSet<Integer> seen = new HashSet<>();
        for(int val : nums){
            if(seen.contains(val))
                return val;

            seen.add(val);
        }

        return -1;
    }

    /*
      Approach 4: Better (Integer Frequency Array Lookups)
      ------------------------------------------------------
      Intuition:
      - Replaces object-based HashSets with low-level array buffers to eliminate heap allocation overhead.
      - Uses values directly as indices inside an auxiliary frequency-counting table.

      Key Idea:
      "Map element values directly to indices in an auxiliary counting array, treating integer flags as visibility markers."

      Step-by-Step:
      1. Allocation: Create an integer array `seen` of the exact same length as `nums`.
      2. Read Sweep: Run through the array values.
      3. Count Lookup: Inspect position `seen[val]`. If it equals 1, the value is a duplicate; return it.
      4. Flag Modification: Otherwise, mark `seen[val] = 1`.

      Complexity Analysis:
      - Time: O(N) -> Direct, single-pass index accesses running in linear time bounds.
      - Space: O(N) -> Allocates an additional primitive integer array matched to the input length.
    */
    private static int findDuplicateBetter2(int[] nums){
        int[] seen = new int[nums.length];
        for(int val : nums){
            if(seen[val] == 1)
                return val;

            seen[val] = 1;
        }

        return -1;
    }

    /*
      Approach 5: Better (Boolean Visited Flag Array)
      ------------------------------------------------------
      Intuition:
      - A minor optimization on Approach 4. Since we only care if an element has been seen once before,
        we can use booleans (`true`/`false`) instead of integers (`0`/`1`) to reduce memory footprints.

      Key Idea:
      "Utilize a dedicated boolean state bit-mask array to flag individual indexing records as visited."

      Step-by-Step:
      1. Array Allocation: Create a primitive `boolean[] seen` array matching the input size.
      2. Loop Pass: Iterate across all values inside `nums`.
      3. State Inspection: Check if `seen[val]` evaluates to `true`. If yes, return `val`.
      4. Update State: If `false`, flip the marker state at `seen[val]` to `true`.

      Complexity Analysis:
      - Time: O(N) -> Single linear pass processing direct pointer operations.
      - Space: O(N) -> Requires allocating an extra boolean tracking space matching the original scale.
    */
    private static int findDuplicateBetter3(int[] nums){
        boolean[] seen = new boolean[nums.length];

        for(int val : nums){
            if(seen[val] == true)
                return val;

            seen[val] = true;
        }

        return -1;
    }

    /*
      Approach 6: Optimized (In-place Negative Value Marking)
      ------------------------------------------------------
      Intuition:
      - Since the array values are strictly bounded between 1 and n, the values can double as valid array indices.
      - Instead of allocating an external array, we can flag values as "seen" by converting the numbers at
        the target indices into negative values.

      Key Idea:
      "Repurpose the array as its own tracking board. Treat values as pointers and flip signs to log visitation history."

      Step-by-Step:
      1. Index Extraction: For each element, find its target index via `Math.abs(num) - 1` (accounting for 0-indexing).
      2. Valuation Check: Inspect the value at `nums[idx]`. If it is negative, this index was visited before; return `Math.abs(num)`.
      3. Flag Inversion: If positive, multiply `nums[idx]` by -1 to lock it down as visited.

      Drawback:
      - This technique mutates the underlying input array, which violates the strict constraints of the problem description.

      Complexity Analysis:
      - Time: O(N) -> One single continuous validation pass over the input.
      - Space: O(1) -> Mutates internal values in-place without using secondary data structures.
    */
    private static int findDuplicateOptimized1(int[] nums){
        for(int num : nums){
            int idx = Math.abs(num) - 1;
            if(nums[idx] < 0)
                return Math.abs(num);

            nums[idx] *= -1;
        }

        return -1;
    }

    /*
      Approach 7: Optimized (Floyd's Cycle Detection / Tortoise & Hare)
      ------------------------------------------------------
      Intuition:
      - If we treat each index as a node and its value `nums[i]` as a pointer to the next index, the array
        maps into a directed linked list structure.
      - Since there is a duplicate number, multiple indices will point to the same next position. This forces
        the linked path to collapse into a cycle. The entrance to this cycle is our duplicate number.

      Key Idea:
      "Use two pointers running at varying speeds to locate a cycle loop, then track down the entrance node
       without modifying any state data."

      Step-by-Step:
      1. Phase 1 (Intersection): Advance `slow` by 1 step (`nums[slow]`) and `fast` by 2 steps (`nums[nums[fast]]`)
         simultaneously until they collide inside the cycle loop.
      2. Phase 2 (Entrance Tracking): Reset `fast` back to the starting node (`nums[0]`). Move both pointers at an
         equal speed of 1 step per iteration.
      3. Termination: The exact node where they meet again is mathematically guaranteed to be the entrance
         to the cycle, which is the duplicate number.

      Dry Run Execution (Using nums = {1, 3, 4, 2, 2}):
      --------------------------------------------------
      Index Mapping:  0->1,  1->3,  2->4,  3->2,  4->2
      Linked Graph:   [0] -> [1] -> [3] -> [2] -> [4]
                                            ^       |
                                            |_______| (Cycle forms here)

      [Phase 1: Finding Collision]
      - Start:      slow = nums[0] = 1,                 fast = nums[0] = 1
      - Iteration 1: slow = nums[1] = 3,                 fast = nums[nums[1]] = nums[3] = 2
      - Iteration 2: slow = nums[3] = 2,                 fast = nums[nums[2]] = nums[4] = 2
      - Collision!  slow == fast (2 == 2). Exit Phase 1 loop.

      [Phase 2: Finding Cycle Entrance]
      - Reset:      slow = 2 (stays at collision),      fast = nums[0] = 1 (resets to start)
      - Iteration 1: slow = nums[2] = 4,                 fast = nums[1] = 3
      - Iteration 2: slow = nums[4] = 2,                 fast = nums[3] = 2
      - Match Found! slow == fast (2 == 2). Cycle entrance found. Return 2.

      Complexity Analysis:
      - Time: O(N) -> The traversal runs within linear constraints across both pointer synchronization loops.
      - Space: O(1) -> Only uses two integer index reference variables. Does not modify the array.
    */
    private static int findDuplicateOptimized2(int[] nums){
        int fast = nums[0];
        int slow = nums[0];

        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        fast = nums[0];
        while(slow != fast){
            slow = nums[slow];
            fast = nums[fast];
        }

        return slow;
    }

}

