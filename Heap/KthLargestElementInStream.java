// https://leetcode.com/problems/kth-largest-element-in-a-stream/description/

import java.util.*;

// Brute-force Approach using List + Sorting (Ascending Order)
/*
  Approach:
  ---------
  1. Store all numbers in a dynamic list.
  2. Every time add(val) is called:
     a) Add val to the list.
     b) Sort the list in ascending order.
     c) Return the element at index (list.size() - k) → kth largest.

  Why it works:
  - Sorting in ascending order places the largest elements at the end.
  - So the kth largest is exactly at index (size - k).

  Complexity Analysis:
  - Time Complexity (add method): O(n log n), because sorting takes O(n log n)
  - Space Complexity: O(n), for storing all numbers in the list
  - Note: Not efficient for large streams or frequent add operations

public class KthLargestElementInStream {
    private List<Integer> list;
    private int k;

    public KthLargestElementInStream(int k, int[] nums){
        this.k = k;
        this.list = new ArrayList<>();

        for(int num : nums){
            list.add(num);
        }
    }

    public int add(int val){
        list.add(val);
        Collections.sort(list);

        return list.get(list.size() - k);
    }

}
*/

// Optimized Approach using Min-Heap (PriorityQueue)
/*
  Approach:
  ---------
  1. Use a min-heap (PriorityQueue) to maintain only the k largest elements seen so far.
     - The heap automatically keeps the smallest element at the root.
     - Whenever we add a new value, if the heap exceeds size k, we remove the smallest element.
     - This way, the heap "kicks out" smaller elements and always retains the k biggest numbers.
  2. Every time add(val) is called:
     a) Add val to the heap.
     b) If heap size > k, remove the smallest element (poll).
     c) The root of the min-heap (peek) is the kth largest element.

  Why it works:
  - The min-heap always contains the k largest elements in the stream.
  - The smallest element in the heap is exactly the kth largest overall.
  - We never need to store all numbers, only the k largest ones.

  Complexity Analysis:
  - Time Complexity (add method): O(log k) → insertion/removal from heap
  - Space Complexity: O(k) → heap stores only k elements
  - Very efficient for large streams and frequent add operations
*/
public class KthLargestElementInStream {

    private int k;
    private PriorityQueue<Integer> minHeap;

    public KthLargestElementInStream(int k, int[] nums) {
        this.k = k;
        this.minHeap = new PriorityQueue<>();

        for(int num : nums)
            add(num);

    }

    public int add(int val) {
        minHeap.offer(val);
        if(minHeap.size() > k)
            minHeap.poll();

        return minHeap.peek();
    }

}