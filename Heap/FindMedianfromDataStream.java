import java.util.*;

/*
 * ============================================================================
 * FIND MEDIAN FROM DATA STREAM: BRUTE FORCE ARCHITECTURE
 * ============================================================================
 *
 * 1. PROBLEM STATEMENT:
 * ---------------------
 * Median is the middle value in an ordered integer list.
 * - If the size of the list is odd, the median is the middle value.
 * - If the size of the list is even, the median is the average of the two middle values.
 *
 * 2. BRUTE FORCE STRATEGY: "SORT ON DEMAND"
 * -----------------------------------------
 * This approach focuses on simplicity of data entry at the cost of retrieval speed.
 *
 * - addNum(int num):
 *   Simply appends the new number to the end of a dynamic array (ArrayList).
 *   Complexity: O(1)
 *
 * - findMedian():
 *   1. Sort the entire array using Collections.sort().
 *   2. Identify the middle index (or indices).
 *   3. Calculate and return the result.
 *   Complexity: O(N log N) per call.
 *
 * 3. LIMITATIONS & BOTTLENECKS:
 * -----------------------------
 * - Efficiency: In a stream of 50,000 numbers, if findMedian is called
 *   frequently, we redundantly sort almost identical arrays thousands of times.
 * - Scalability: As the stream grows, the sorting cost becomes a massive
 *   performance bottleneck (O(N log N) vs the optimal O(log N)).
 *
 * ============================================================================
 */
//public class FindMedianfromDataStream {
//    private List<Integer> nums;
//
//    public FindMedianfromDataStream(){
//        nums = new ArrayList<>();
//    }
//
//    public void addNum(int num){
//        nums.add(num);
//    }
//
//    public double findMedian(){
//        Collections.sort(nums);
//        int n = nums.size();
//
//        if(n % 2 == 0){
//            return (nums.get(n/2-1) + nums.get(n/2)) / 2.0;
//        }else{
//            return (double) nums.get(n/2);
//        }
//    }
//
//}


// OPTIMIZED APPROACH:
/*
 * ============================================================================
 * FIND MEDIAN FROM DATA STREAM: TWO HEAPS ARCHITECTURE
 * ============================================================================
 *
 * 1. DESIGN STRATEGY:
 * -------------------
 * Instead of sorting the entire list, we maintain two heaps:
 * - small (Max-Heap): Stores the smaller half of the numbers.
 * - large (Min-Heap): Stores the larger half of the numbers.
 *
 * This effectively keeps the "middle" of the data at the roots of the heaps.
 *
 * 2. THE BALANCING LOGIC:
 * -----------------------
 * To ensure the heaps represent the two halves correctly:
 * - Every number is first filtered through the Max-Heap.
 * - The largest of the small numbers is then passed to the Min-Heap.
 * - If the Min-Heap becomes larger than the Max-Heap, we move one back.
 * - Result: small.size() >= large.size(), and the difference is at most 1.
 *
 * 3. COMPLEXITY:
 * --------------
 * - addNum: O(log N) -> Inserting into a heap.
 * - findMedian: O(1) -> Simply looking at the roots (peek).
 * ============================================================================
 */
public class FindMedianfromDataStream {
    private PriorityQueue<Integer> left;    // max heap
    private PriorityQueue<Integer> right;   // min heap

    public FindMedianfromDataStream(){
        left = new PriorityQueue<>(Collections.reverseOrder()); // max heap
        right = new PriorityQueue<>();                          // min heap
    }

    public void addNum(int num){
        left.offer(num);

        // the largest of left belongs to right
        right.offer(left.poll());

        // if right is bigger, move it to left so that left.size() >= right.size()
        if(right.size() > left.size())
            left.offer(right.poll());
    }

    public double findMedian(){
        if(left.size() > right.size())
            return left.peek();

        return (left.peek() + right.peek()) / 2.0;
    }
}