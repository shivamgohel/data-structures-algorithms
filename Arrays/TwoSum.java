// https://leetcode.com/problems/two-sum/description/
import java.util.*;

class TwoSum
{
    public static void main(String[] args) 
    {

        /*
          ================================================================================
          PROBLEM STATEMENT:
          Given an array of integers `nums` and an integer `target`, return indices of the
          two numbers such that they add up to `target`.

          Constraints:
          - 2 <= nums.length <= 10^4
          - -10^9 <= nums[i] <= 10^9
          - -10^9 <= target <= 10^9
          - Only one valid answer exists.
          ================================================================================
         */

        int[] nums = {0,4,6,5};
        int target = 7;

        // Java does not override toString() for arrays. (prints the address of array)
        // Arrays.toString(array) converts the array into a human-readable string format.
        // It prints the elements inside square brackets.

        // Approach 1: Brute Force Primitive Array | TC: O(N^2), SC: O(1)
        System.out.println(Arrays.toString(twoSumBrute(nums,target)));

        // Approach 2: Brute Force List Object Output | TC: O(N^2), SC: O(1)
        System.out.println(twoSumBrute2(nums, target));

        // Approach 3: HashMap Double-Pass Lookups | TC: O(N), SC: O(N)
        System.out.println(Arrays.toString(twoSumOptimized1(nums,target)));

        // Approach 4: HashMap Single-Pass Interception | TC: O(N), SC: O(N)
        System.out.println(Arrays.toString(twoSumOptimized2(nums, target)));

    }

    /*
      Approach 1: Brute Force (Nested Loops - Primitive Output)
      -------------------------------------------------------
      Intuition:
      - Scan every pair configuration across the linear sequence to test their sum match.
      - A basic fallback approach when no auxiliary tracking structures are permitted.

      Key Idea:
      "Exhaustively check all unique element combinations via dual index frame testing."

      Step-by-Step:
      1. Outer Loop: Establish baseline pointer `i` from start to end.
      2. Inner Loop: Move ahead starting precisely at index `j = i + 1`.
      3. Sum Check: If `nums[i] + nums[j] == target`, instantiate and return `new int[]{i, j}`.
      4. Fallback: Return `new int[]{-1, -1}` if loops complete empty.

      Complexity Analysis:
      - Time: O(N^2) -> Double nested iterations across the full structural dimension.
      - Space: O(1) -> Runs entirely in-place utilizing static pointer values.
    */
    private static int[] twoSumBrute(int[] nums, int target) {
        for(int i=0;i<nums.length;i++)
        {
            for(int j=i+1;j<nums.length;j++)
            {
                if((nums[i] + nums[j]) == target)
                    return new int[]{i , j};
            }
        }
        return new int[]{-1,-1};
    }

    /*
      Approach 2: Brute Force (Nested Loops - Object List Output)
      -------------------------------------------------------
      Intuition:
      - Leverages identical loop search patterns as Approach 1.
      - Transforms internal code logic to adhere to dynamic Collections frameworks rather than arrays.

      Key Idea:
      "Utilize factory collection wrappers to return immutable object pairs instantly."

      Step-by-Step:
      1. Run nested searches for pairs adding up to the target value.
      2. On capture: Return via factory construct `List.of(i, j)`.
      3. On default fail: Return default indicators via `List.of(-1, -1)`.

      Complexity Analysis:
      - Time: O(N^2) -> Nested checks drive quadratic growth constraints.
      - Space: O(1) -> Primitive tracking metrics; list structures are generated on execution escape.
    */
    private static List<Integer> twoSumBrute2(int[] nums, int target){
        for(int i=0; i<nums.length; i++){
            for(int j=i+1; j<nums.length; j++){
                if(nums[i] + nums[j] == target){
                    // return Arrays.asList(i, j);
                    return List.of(i,j);
                }
            }
        }

        // return Arrays.asList(-1, -1);
        return List.of(-1, -1);
    }


    /*
      Approach 3: Optimized (HashMap Double-Pass Lookups)
      ------------------------------------------------------
      Intuition:
      - Map tracking provides direct constant-time data lookups.
      - Splitting creation and processing makes checking clear and separates concerns.

      Key Idea:
      "Pre-build an absolute indexing registry to allow instant reverse-complement querying."

      Step-by-Step:
      1. First Pass: Complete a sweep to populate a `HashMap` mapping `[Value -> Index]`.
      2. Second Pass: Iterate through indices checking for `diff = target - nums[i]`.
      3. Validation: Verify map contains `diff` and ensure `hm.get(diff)` does not match current pointer index `i`.

      Complexity Analysis:
      - Time: O(N) -> Executed over two explicit linear passes ($O(N) + O(N) = O(N)$).
      - Space: O(N) -> Space allocation matches unique collection entry sets.
    */
    private static int[] twoSumOptimized1(int[] nums, int target) {
        HashMap<Integer,Integer> hm = new HashMap<>();

        for(int i=0;i<nums.length;i++)
        {
            hm.put(nums[i], i);
        }

        for(int i=0;i<nums.length;i++)
        {
            int diff = target - nums[i];
            if(hm.containsKey(diff) && hm.get(diff) != i)
            {
                return new int[]{i , hm.get(diff)};
            }
        }

        return new int[]{-1,-1};
    }

    /*
      Approach 5: Optimized (HashMap Single-Pass Interception)
      ------------------------------------------------------
      Intuition:
      - You don't need to load everything up front. You can check elements dynamically *as you build* the map.
      - Looking backward at what's already saved naturally prevents an element from matching itself.

      Key Idea:
      "Query historical records for the complement value before logging the current item into memory."

      Step-by-Step:
      1. Initialize: Launch a singular tracker `HashMap`.
      2. Loop Sweep: At each position, find the target complement requirement (`diff = target - nums[i]`).
      3. Look Backward: Query map via `containsKey(diff)`. If found, capture index positions and return immediately.
      4. Log Progress: If missing, commit current values to memory mapping `hm.put(nums[i], i)` and move forward.

      Complexity Analysis:
      - Time: O(N) -> Traverses sequence once; lookups and insertions operate at constant O(1) complexity.
      - Space: O(N) -> Map bounds adjust dynamically up to worst-case footprint limitations.
    */
    private static int[] twoSumOptimized2(int[] nums, int target) {
        HashMap<Integer,Integer> hm = new HashMap<>();

        for(int i=0;i<nums.length;i++)
        {
            int diff = target - nums[i];

            // Clean single-lookup validation hook.
            // Note: Checking 'hm.get(diff) != i' is redundant here because the current element
            // has not been added to the map yet, preventing any element from matching itself.
            if(hm.containsKey(diff))
            {
                return new int[]{hm.get(diff) , i};
            }

            hm.put(nums[i], i);
        }
        return new int[]{-1,-1};
    }

}