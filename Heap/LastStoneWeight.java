// https://leetcode.com/problems/last-stone-weight/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class LastStoneWeight {
    public static void main(String[] args){

        /*
          Problem Statement:
          -----------------
          You are given an array of integers `stones` where stones[i] is the weight of the ith stone.
          We play a game with the stones. In each turn, we choose the **two heaviest stones** and smash them together.
          Suppose the heaviest stones have weights `x` and `y` with `x <= y`:
          - If x == y, both stones are destroyed.
          - If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight (y - x).

          At the end of the game, there is at most one stone left.
          Return the weight of the last remaining stone. If no stones are left, return 0.

          Example:
          --------
          Input: stones = [2,7,4,1,8,1]
          Output: 1
          Explanation:
          1. Smash 8 and 7 -> 1 remains. Stones: [2,4,1,1,1]
          2. Smash 4 and 2 -> 2 remains. Stones: [2,1,1,1]
          3. Smash 2 and 1 -> 1 remains. Stones: [1,1,1]
          4. Smash 1 and 1 -> 0 remains. Stones: [1]
          5. Final stone is 1.
        */

        int[] stones = {2,7,4,1,8,1};

        // Approach 1: Brute-force → Repeated Sorting → TC: O(n^2 log n), SC: O(n)
        System.out.println(lastStoneWeightBrute(stones));

        // Approach 2: Max-Heap (Optimized) → O(n log n) → Best for tracking "top" elements
        System.out.println(lastStoneWeightOptimized(stones));

    }

    /*
      Approach 1: Brute-force (Repeated Sorting)
      -----------------------------------------
      Problem:
      - We need the two heaviest stones in every step.

      Intuition:
      - If we sort the list, the heaviest stones are always at the front (or back).
      - After smashing, the new stone might change the order, so we re-sort.

      Step-by-Step Thought Process:
      -----------------------------
      1. Convert `int[]` to a `List<Integer>` to allow easy removal/addition.
      2. Loop while more than 1 stone exists:
         a. Sort the list in descending order.
         b. Remove the two largest stones (indices 0 and 1).
         c. If they aren't equal, add the difference back to the list.
      3. Return the last element if it exists, otherwise 0.

      Complexity Analysis:
      --------------------
      - Time: O(n^2 log n) → We sort (n log n) up to n times.
      - Space: O(n) → To store the list.
    */
    private static int lastStoneWeightBrute(int[] stones){
        List<Integer> list = new ArrayList<>();
        for(int x : stones)
            list.add(x);

        while(list.size() > 1){
            Collections.sort(list, (a, b) -> b - a);

            int y = list.remove(0);
            int x = list.remove(0);

            if(y != x)
                list.add(y - x);
        }

        return list.isEmpty() ? 0 : list.get(0);
    }

    /*
      Approach 2: Max-Heap (Optimized)
      -------------------------------
      Problem:
      - Repeatedly fetching the "Max" element from a changing set of data.
      - Sorting O(n \log n) repeatedly is overkill.

      Intuition:
      - A **Priority Queue (Max-Heap)** is designed exactly for this.
      - It allows us to extract the maximum element and insert new elements in O(log n) time.



      Step-by-Step Thought Process:
      -----------------------------
      1. Initialize a `PriorityQueue` with a reverse comparator to act as a **Max-Heap**.
      2. Add all stone weights into the heap.
      3. While heap size > 1:
         a. `poll()` the two heaviest stones (let's call them `y` and `x`).
         b. If `y != x`, `offer()` the result (y - x) back into the heap.
         c. The heap automatically re-arranges itself in O(\log n).
      4. Return the top of the heap if not empty, otherwise 0.

      Complexity Analysis:
      --------------------
      - Time: O(n log n)
      - Space: O(n) → To store stones in the heap.
    */
    private static int lastStoneWeightOptimized(int[] stones){
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
                (a,b) ->  Integer.compare(b,a)
        );
        for(int x : stones)
            maxHeap.offer(x);

        while(maxHeap.size() > 1){
            int y = maxHeap.poll();
            int x = maxHeap.poll();

            if(y != x)
                maxHeap.offer(y - x);
        }

        return maxHeap.isEmpty() ? 0 : maxHeap.peek();
    }

}
