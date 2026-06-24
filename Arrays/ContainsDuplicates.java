// https://leetcode.com/problems/contains-duplicate/description/
import java.util.*;

public class ContainsDuplicates
{
    public static void main(String[] args)
    {
        /*
          ================================================================================
          PROBLEM STATEMENT:
          Given an integer array `nums`, return true if any value appears at least twice
          in the array, and false if every element is distinct.

          Constraints:
          - 1 <= nums.length <= 10^5
          - -10^9 <= nums[i] <= 10^9
          ================================================================================
         */
       
        int nums[] = {1,2,3,3};

        // Approach 1: Brute Force | TC: O(N^2), SC: O(1)
        System.out.println(containsDuplicateBrute(nums));

        // Approach 2: Sorting | TC: O(N log N), SC: O(1) or O(N) depending on sort
        System.out.println(containsDuplicateBetter(nums));

        // Approach 3: HashSet with Early Exit | TC: O(N), SC: O(N) Auxiliary Space
        System.out.println(containsDuplicateOptimzed1(nums));

        // Approach 4: Bulk Set Size Comparison | TC: O(N), SC: O(N)
        System.out.println(containsDuplicateOptimzed2(nums));

        // Approach 5: HashMap Counter Tracking | TC: O(N), SC: O(N)
        System.out.println(containsDuplicateOptimzed3(nums));

        // Approach 6: HashSet Set-Add Trick | TC: O(N), SC: O(N)
        System.out.println(containsDuplicateOptimzed4(nums));

        // Approach 7: Functional Java Streams | TC: O(N), SC: O(N)
        System.out.println(containsDuplicateOptimzed5(nums));

    }

     /*
      Approach 1: Brute Force (Nested Loop Comparison)
      -------------------------------------------------------
      Intuition:
      - Compare every element in the array with every other element that follows it.
      - If any pair matching criteria `nums[i] == nums[j]` is found, a duplicate exists.

      Key Idea:
      "Exhaustively check all unique element pairings via localized pointer comparisons."

      Step-by-Step:
      1. Outer Loop: Iterate through each element as the base reference pointer `i`.
      2. Inner Loop: Scan ahead from index `i + 1` to the end of the array using pointer `j`.
      3. Match Check: If `nums[i] == nums[j]`, immediately return true.
      4. Termination: If the loops complete without finding a match, return false.

      Why it's an anti-pattern for interviews:
      - Extreme scaling inefficiencies on larger datasets ($10^5$ items will trigger $10^{10}$ operations).

      Complexity Analysis:
      - Time: O(N^2) -> Two nested iteration sweeps over the collection sequence.
      - Space: O(1) -> Performs lookup in place using fixed-space primitive pointers.
    */
    private static boolean containsDuplicateBrute(int[] nums) {
        for(int i=0;i<nums.length;i++)
        {
            for(int j=i+1;j<nums.length;j++)
            {
                if(nums[i] ==  nums[j])
                    return true;
            }
        }
        return false;
    }

    /*
      Approach 2: Better (Sorting Adjacent Values)
      ------------------------------------------------------
      Intuition:
      - Sorting elements forces identical values to cluster directly next to each other.
      - Once the collection sequence is ordered, we no longer need to look ahead multiple frames.

      Key Idea:
      "Re-order the array to transform a global lookup problem into a localized neighbor check."

      Step-by-Step:
      1. Sort: Call `Arrays.sort()` to organize elements in ascending sequence.
      2. Single Pass: Iterate starting from index 1 up to the length of the array.
      3. Neighbor Check: Inspect if the current value matches the previous value (`nums[i] == nums[i-1]`).

      Complexity Analysis:
      - Time: O(N log N) -> Driven entirely by Dual-Pivot Quicksort / Timsort overhead.
      - Space: O(1) or O(N) -> O(1) if sorting primitive arrays in-place via Quicksort variants, though log(N) stack frames apply.
    */
    private  static boolean containsDuplicateBetter(int[] nums) {
        Arrays.sort(nums);
        
        for(int i=1;i<nums.length;i++)
        {
            if(nums[i-1] == nums[i])
                return true;
        }
        return false;
    }

     /*
      Approach 3: Optimized (HashSet with Explicit Early Exit)
      ------------------------------------------------------
      Intuition:
      - Hash-based lookups offer near-instantaneous search speeds.
      - Tracking elements inside an external memory frame allows us to intercept duplicates on the fly.

      Key Idea:
      "Query a dynamic lookup buffer before recording elements to catch duplicates instantly."

      Step-by-Step:
      1. Initialize: Spin up a generic tracking `HashSet`.
      2. Stream-Through: Scan left-to-right over each array element.
      3. Containment Check: If the set already holds the element, fail-fast and return true.
      4. Record: Otherwise, add it to the tracked collection frame and proceed.

      Complexity Analysis:
      - Time: O(N) -> Single pass with instantaneous O(1) average lookup and store operations.
      - Space: O(N) -> Memory tracking scales linearly with the number of unique elements stored.
    */
    private static boolean containsDuplicateOptimzed1(int[] nums) {
        HashSet<Integer> seen = new HashSet<>();

        for(int x : nums)
        {
            if(seen.contains(x))
                return true;
            seen.add(x);    
        }
        return false; 
    }

    /*
      Approach 4: Optimized (Bulk Set Size Comparison)
      ------------------------------------------------------
      Intuition:
      - Mathematical Sets completely strip away element duplication by design.
      - If the structural size of the finalized set matches the array size, all values are distinct.

      Key Idea:
      "Dump all elements into a Set structure and contrast dimensions directly."

      Step-by-Step:
      1. Collection Dump: Populate a `HashSet` sequentially using a clean loop pattern.
      2. Size Evaluation: Evaluate if the absolute length of `nums` matches the underlying size of the populated set.

      Drawback:
      - Lacks short-circuiting capacity. It processes the whole collection even if index 0 and 1 match.

      Complexity Analysis:
      - Time: O(N) -> Always sweeps the entire sequence without dynamic short-circuit paths.
      - Space: O(N) -> Collects allocations matching the distinct key footprint.
    */
    private static boolean containsDuplicateOptimzed2(int[] nums) {
        HashSet<Integer> seen2 = new HashSet<>();

        for(int x : nums)
            seen2.add(x);

        // if nums.length == seen2.size() -> duplicate doesnt exist --> return false
        return nums.length != seen2.size();    
    }

    /*
      Approach 5: Optimized (HashMap Counter Tracking)
      ------------------------------------------------------
      Intuition:
      - Use key-value mappings to register actual frequency buckets for each tracking index.
      - Useful for generic modifications where exact occurrence numbers matter.

      Key Idea:
      "Map out tracking buckets to count individual element occurrences explicitly."

      Step-by-Step:
      1. Initialize: Establish a tracking framework with a new `HashMap`.
      2. Record Frequency: Iterate over the elements, putting the item combined with its current baseline count + 1.
      3. Over-Count Check: Evaluate if the mapped balance for the element crosses the strict uniqueness threshold of 1.

      Complexity Analysis:
      - Time: O(N) -> Single directional array sweep utilizing efficient map hashing operations.
      - Space: O(N) -> Allocates up to N entries for storing discrete key-value counts.
    */
    private static boolean containsDuplicateOptimzed3(int[] nums){
        Map<Integer, Integer> map = new HashMap<>();

        for(int num : nums){
            map.put(num, map.getOrDefault(num, 0) +1);

            if(map.get(num) > 1)
                return true;
        }

        return false;
    }

    /*
      Approach 6: Optimized (HashSet Set-Add Trick)
      ------------------------------------------------------
      Intuition:
      - The internal contract of Java's `Set.add()` returns a boolean status value.
      - If the item already exists in the set, the operation returns false instead of inserting.

      Key Idea:
      "Leverage the return payload of the mutation method to compress checking and inserting into one op."

      Step-by-Step:
      1. Initialize: Spin up an empty tracking container `HashSet`.
      2. Direct Check: Loop through the array and immediately call `set.add(num)`.
      3. Intercept: If `set.add(num)` evaluates to false, a duplicate is verified; return true.
      4. Safe Return: Return false if the collection exhausts smoothly.

      Complexity Analysis:
      - Time: O(N) -> Single pass with optimal execution branches.
      - Space: O(N) -> Allocates a dynamic mapping segment matching the unique sequence width.
    */
    private static boolean containsDuplicateOptimzed4(int[] nums){
        HashSet<Integer> set = new HashSet<>();

        for(int num : nums){
            if(!set.add(num))
                return true;
        }

        return false;
    }

    /*
      Approach 7: Optimized (Functional Java Streams)
      ------------------------------------------------------
      Intuition:
      - Employs modern functional programming features to declare the logical filter intent.
      - Uses declarative pipelines to evaluate uniqueness constraints elegantly.

      Key Idea:
      "Use functional language stream pipelines to perform automatic reduction filters."

      Step-by-Step:
      1. Open Stream: Construct an internal tracking framework using `Arrays.stream(nums)`.
      2. Apply Filter: Invoke `.distinct()` to create a structural pipe containing exclusively distinct keys.
      3. Aggregate: Query the processed size using `.count()` and verify if it is smaller than the input footprint.

      Complexity Analysis:
      - Time: O(N) -> Traverses the stream pipeline processing unique occurrences sequentially.
      - Space: O(N) -> Stream tracking allocations mimic standard lookup buffer limits.
    */
    private static boolean containsDuplicateOptimzed5(int[] nums){
        return Arrays.stream(nums)
                     .distinct()
                     .count() < nums.length;
    }

}