import java.util.*;

public class FrequencySort {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given an array of integers, sort the array based on the **frequency of elements in descending order**.
         If two elements have the same frequency, sort by **value in ascending order**.

         Example:
         Input:  [4, 4, 1, 2, 2, 2, 3]
         Output: [2, 2, 2, 4, 4, 1, 3]
        */

        int[] arr = {4, 4, 1, 2, 2, 2, 3};

        // Approach 1: Sort using Comparator on array → TC: O(n log n), SC: O(n)
        int[] sortedArr1 = frequencySortArray(arr.clone());
        System.out.println(Arrays.toString(sortedArr1));

        // Approach 2: Using Heap (PriorityQueue) with Frequency Map → TC: O(n log k), SC: O(n)
        int[] sortedArr2 = frequencySortHeap(arr.clone());
        System.out.println(Arrays.toString(sortedArr2));

    }

    /*
     Approach 1: Sort Using Comparator (List version)
     ---------------------------------

     Idea and Thought Process:
     1. Count frequency of each element using a Map.
     2. Convert array to List<Integer> to sort with comparator:
        - Compare by frequency **descending** (higher frequency first).
        - If frequency equal, compare by value **ascending**.
     3. Convert List back to array.

     Why it works:
     - Comparator ensures correct order by frequency and value.
     - Sorting rearranges elements correctly.

     Complexity Analysis:
     - Time Complexity: O(n log n) for sorting the list
     - Space Complexity: O(n) for frequency map and list
    */
    private static int[] frequencySortArray(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for(int num : nums)
            freqMap.put(num, freqMap.getOrDefault(num, 0) +1);

        // convert the nums array into list
        List<Integer> list = new ArrayList<>();
        for(int num : nums) list.add(num);

        list.sort((a,b) -> {
           int freqCompare = freqMap.get(b) - freqMap.get(a);
           if(freqCompare != 0) return freqCompare;

           return a - b;
        });

        // conver back the list into nums array
        for(int i=0; i<list.size(); i++)
            nums[i] = list.get(i);

        return nums;
    }


    /*
     Approach 2: Heap (PriorityQueue) with Frequency Map
     ---------------------------------------------------

     Idea and Thought Process:
     1. Count frequency of each element using a Map.
     2. Add unique elements into a heap (priority queue) with comparator:
        - Compare by frequency **descending** (max frequency first)
        - Value ascending if frequency equal
     3. Poll elements from heap and add to result array frequency times.

     Why Heap helps:
     - Heap ensures we always get the element with **highest frequency first**.
     - Automatically handles ordering for elements with same frequency.

     Steps:
     a) Build frequency map.
     b) Build max-heap with comparator by freq descending, value ascending.
     c) Initialize result array index.
     d) While heap not empty:
        - Poll element
        - Fill result array freq times

     Complexity Analysis:
     - Time Complexity: O(n log k), where k = number of unique elements
       (Building heap takes O(k), polling k elements each freq times is O(n))
     - Space Complexity: O(n) for frequency map and heap
    */
    private static int[] frequencySortHeap(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for(int num : nums)
            freqMap.put(num, freqMap.getOrDefault(num, 0) +1);

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a,b) -> {
            int freqCompare = freqMap.get(b) - freqMap.get(a);
            if(freqCompare != 0) return freqCompare;

            return a - b;
        });

        // push into maxHeap from the freqMap
        for(int num : freqMap.keySet())
            maxHeap.offer(num);

        // copy to res array from heap
        int[] res = new int[nums.length];
        int idx = 0;
        while(!maxHeap.isEmpty()){
            int val = maxHeap.poll();
            int freq = freqMap.get(val);

            for(int i=0; i<freq; i++)
                res[idx++] = val;
        }

        return res;
    }
}

/*
    Dry Run - Approach 1: Comparator Sort
    ----------------------------------------------

    Input Array: [4, 4, 1, 2, 2, 2, 3]

    Step 1: Build Frequency Map
    ---------------------------
    Count how many times each element appears in the array.

    Iterating over the array:
    - 4 → frequency = 1
    - 4 → frequency = 2
    - 1 → frequency = 1
    - 2 → frequency = 1
    - 2 → frequency = 2
    - 2 → frequency = 3
    - 3 → frequency = 1

    Frequency Map:
    {1:1, 2:3, 3:1, 4:2}

    Step 2: Convert array to List<Integer> for sorting
    --------------------------------------------------
    List: [4, 4, 1, 2, 2, 2, 3]

    Step 3: Sort with Comparator
    - Comparator rules:
      1. Frequency descending (higher frequency first)
      2. If frequency tie, value ascending (smaller number first)

    Detailed Comparison Example:
    - Compare 4 vs 4 → freq(4)=2, freq(4)=2 → tie → values equal → no change
    - Compare 4 vs 1 → freq(4)=2, freq(1)=1 → 4 comes first
    - Compare 2 vs 4 → freq(2)=3, freq(4)=2 → 2 comes first
    - Compare 1 vs 3 → freq(1)=1, freq(3)=1 → tie → 1 < 3 → 1 comes first
    - Continue until fully sorted.

    Step 4: Sorted List → Convert back to array
    [2, 2, 2, 4, 4, 1, 3]

    Step 5: Final Output
    --------------------
    [2, 2, 2, 4, 4, 1, 3]
*/
/*
    Dry Run - Approach 2: Heap (PriorityQueue)
    ---------------------------------------------------

    Input Array: [4, 4, 1, 2, 2, 2, 3]

    Step 1: Build Frequency Map
    ---------------------------
    Count how many times each element appears in the array.

    Frequency Map:
    {1:1, 2:3, 3:1, 4:2}

    Step 2: Build Max-Heap of Unique Elements
    -----------------------------------------
    Heap Comparator rules:
    1. Frequency descending (higher frequency first)
    2. If frequency tie, value ascending

    Add unique elements to heap: [1, 2, 3, 4]
    After heapify (max-heap by frequency, value tie-breaker):
    Heap order (conceptual, top = max frequency): [2, 4, 1, 3]

    Step 3: Poll elements and fill result array
    -------------------------------------------
    Initialize result array: [_, _, _, _, _, _, _]

    - Poll 2 → frequency = 3 → add 2, 2, 2
      Result: [2, 2, 2, _, _, _, _]

    - Poll 4 → frequency = 2 → add 4, 4
      Result: [2, 2, 2, 4, 4, _, _]

    - Poll 1 → frequency = 1 → add 1
      Result: [2, 2, 2, 4, 4, 1, _]

    - Poll 3 → frequency = 1 → add 3
      Result: [2, 2, 2, 4, 4, 1, 3]

    Step 4: Final Output
    --------------------
    [2, 2, 2, 4, 4, 1, 3]

    Explanation:
    - Elements with higher frequency come first.
    - If frequencies are equal, smaller values appear first.
    - Heap ensures the ordering is maintained efficiently.
*/

