// https://leetcode.com/problems/k-closest-points-to-origin/description/

import java.util.Arrays;
import java.util.PriorityQueue;

public class KClosestPointsToOrigin {
    public static void main(String[] args) {

        /*
          Problem Statement:
          -----------------
          Given an array of `points` where points[i] = [xi, yi] represents a point on the X-Y plane
          and an integer `k`, return the `k` closest points to the origin (0, 0).

          The distance between two points on the X-Y plane is the Euclidean distance:
          sqrt((x1 - x2)^2 + (y1 - y2)^2)
          Since we are comparing distances from the origin (0,0), the formula simplifies to:
          sqrt(x^2 + y^2)
          Note: We can compare (x^2 + y^2) directly to avoid expensive square root calculations.

          Example:
          --------
          Input: points = [[3,3],[5,-1],[-2,4]], k = 2
          Output: [[3,3],[-2,4]]
          Explanation:
          1. Dist squared of (3,3) = 3^2 + 3^2 = 18
          2. Dist squared of (5,-1) = 5^2 + (-1)^2 = 26
          3. Dist squared of (-2,4) = (-2)^2 + 4^2 = 20
          The 2 closest points are (3,3) and (-2,4).
        */

        int[][] points = {{3,3},{5,-1},{-2,4}};
        int k = 2;

        // Approach 1: Brute-force -> Sort all points -> TC: O(n log n), SC: O(log n)
        System.out.println(Arrays.deepToString(kClosestBrute(points, k)));

        // Approach 2: Max-Heap -> Maintain only K elements -> TC: O(n log k), SC: O(k
        System.out.println(Arrays.deepToString(kClosestOptimized(points, k)));

    }

    /*
      Approach 1: Brute-force (Full Sorting)
      -------------------------------------
      Intuition:
      - If we sort all points based on their distance from the origin,
        the first `k` points will naturally be the closest.

      Step-by-Step Thought Process:
      -----------------------------
      1. Use Arrays.sort() with a custom comparator.
      2. For any two points `a` and `b`, calculate (x^2 + y^2).
      3. Sort in ascending order (closest first).
      4. Use Arrays.copyOfRange to return the first `k` elements.

      Complexity Analysis:
      --------------------
      - Time: O(n log n) -> Sorting the entire array of size n.
      - Space: O(log n) -> Space used by the sorting algorithm (Pivot stack).
    */
    private static int[][] kClosestBrute(int[][] points, int k){
        Arrays.sort(points, (a,b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];

            return Integer.compare(distA, distB);
        });

        return Arrays.copyOfRange(points, 0, k);
    }

    /*
      Approach 2: Max-Heap (Optimized for K)
      -------------------------------------
      Problem:
      - Sorting all n points is inefficient if n is very large and k is very small.

      Intuition:
      - We only care about the k smallest distances.
      - A Max-Heap of size k can keep track of the "smallest seen so far" among our candidates.
      - The root (top) of a Max-Heap is always the largest element. By keeping the heap size at k,
        the root represents the "farthest" point of our k-closest candidates.
      - If a new point is closer than this root, we remove the root and add the new point.

      Dry Run (Example: points = [[3,3], [5,-1], [-2,4]], k = 2):
      ---------------------------------------------------------
      Distances squared: (3,3)=18, (5,-1)=26, (-2,4)=20

      1. Process [3,3] (Dist: 18):
         - Add to heap. Heap: [18] (Size 1)
      2. Process [5,-1] (Dist: 26):
         - Add to heap. Heap: [26, 18] (Size 2, 26 is at top)
      3. Process [-2,4] (Dist: 20):
         - Add to heap. Heap: [26, 20, 18] (Size 3)
         - Size > k (2), so poll() the top.
         - 26 is removed. Heap: [20, 18] (Size 2)

      Final Heap contains distances 20 and 18, which correspond to [-2,4] and [3,3].

      Step-by-Step Thought Process:
      -----------------------------
      1. Initialize a PriorityQueue as a Max-Heap (farthest point at the top).
      2. Iterate through every point:
         a. Add point to the heap.
         b. If heap size exceeds k, poll() the top (removes the point with the largest distance).
      3. The heap now contains exactly the k closest points.
      4. Transfer points from the heap to a result array.

      Complexity Analysis:
      --------------------
      - Time: O(n log k) -> Each of the n insertions/extractions takes log(k) time.
      - Space: O(k) -> To store k points in the heap.
    */
    private static int[][] kClosestOptimized(int[][] points, int k){
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a,b) -> {
            int distA = a[0] * a[0] + a[1] * a[1];
            int distB = b[0] * b[0] + b[1] * b[1];

            return Integer.compare(distB, distA);
        });

        for(int[] point : points){
            maxHeap.offer(point);

            if(maxHeap.size() > k)
                maxHeap.poll();
        }

        int[][] res = new int[k][2];
        int idx = k-1;
        while(!maxHeap.isEmpty()){
            res[idx] = maxHeap.poll();
            idx--;
        }

        return res;
    }
}
