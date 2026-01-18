// https://leetcode.com/problems/minimum-interval-to-include-each-query/description/

import java.util.*;

public class MinimumIntervaltoIncludeEachQuery {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given a list of intervals [start, end] and a list of queries,
         for each query, find the **size of the smallest interval** that includes it.
         If no interval covers the query, return -1.

         Example:
         Input: intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]
         Output: [3,3,1,4]
         Explanation:
         - Query 2 → intervals [1,4], [2,4] include it → min size = 4-1+1 = 4
         - Query 3 → intervals [1,4], [2,4], [3,6] → min size = 3
         - Query 4 → interval [4,4] → size 1
         - Query 5 → interval [3,6] → size 4
        */

        int[][] intervals = {{1,4},{2,4},{3,6},{4,4}};
        int[] queries = {2,3,4,5};

        // Brute-force approach → TC: O(Q*N), SC: O(Q)
        System.out.println(Arrays.toString(minIntervalBrute(intervals, queries)));

        // Optimized heap-based approach → TC: O(N log N + Q log N), SC: O(N + Q)
        System.out.println(Arrays.toString(minIntervalOptimized(intervals, queries)));

    }

    /*
     Approach 1: Brute-force
     -----------------------
     Problem:
     - Given a set of intervals and queries, for each query we need the **smallest interval** that includes it.
     - A query may not be covered by any interval → return -1 in that case.

     Intuition / Why Brute-force:
     -----------------------------
     - The simplest, most direct solution is to **check each query against all intervals**.
     - For each query, find all intervals that cover it and keep track of the minimum size.

     Step-by-Step Thought Process:
     -----------------------------
     1. For each query:
        a) Initialize `minSize = ∞`
        b) Iterate through all intervals:
           - If `interval.start <= query <= interval.end`:
               • size = interval.end - interval.start + 1
               • update minSize if smaller
        c) If `minSize` = ∞ → no interval covers the query → result = -1
           Else → result = minSize

     Example Walkthrough:
     --------------------
     Intervals: [[1,4], [2,4], [3,6], [4,4]]
     Queries: [2,3,4,5]

     Query 1:
     - Covered by [1,4] (size 4), [2,4] (size 3)
     - Minimum size = 3 → result[0] = 3

     Query 2:
     - Covered by [1,4] (4), [2,4] (3), [3,6] (4)
     - Minimum size = 3 → result[1] = 3

     Query 3:
     - Covered by [1,4] (4), [2,4] (3), [3,6] (4), [4,4] (1)
     - Minimum size = 1 → result[2] = 1

     Query 4:
     - Covered by [3,6] (4)
     - Minimum size = 4 → result[3] = 4

     Complexity Analysis:
     --------------------
     - Time: O(Q * N), where Q = number of queries, N = number of intervals
     - Space: O(Q) for the result array
    */
    private static int[] minIntervalBrute(int[][] intervals, int[] queries){
        int numberOfQueries = queries.length;
        int[] result = new int[numberOfQueries];

        for(int queryIndex=0; queryIndex<numberOfQueries; queryIndex++){
            int currentQuery = queries[queryIndex];
            int minimumSize = Integer.MAX_VALUE;

            for(int[] interval : intervals){
                int intervalStart = interval[0];
                int intervalEnd = interval[1];

                if(intervalStart <= currentQuery && currentQuery <= intervalEnd){
                    int intervalSize = intervalEnd - intervalStart + 1;
                    minimumSize = Math.min(minimumSize, intervalSize);
                }
            }


            if(minimumSize == Integer.MAX_VALUE){
                result[queryIndex] = -1;
            }else{
                result[queryIndex] = minimumSize;
            }
        }

        return result;
    }

    /*
     Approach 2: Min-Heap (Optimized)
     ---------------------------------
     Problem:
     - Given intervals and queries, find the **smallest interval** covering each query.
     - Some queries may not be covered by any interval → return -1 for those.

     Intuition / Why Heap:
     ----------------------
     - We want to quickly find the **smallest interval that covers a query**.
     - Key observations:
       1. Intervals with start > query cannot cover it → ignore them.
       2. Intervals with end < query cannot cover it → remove them.
       3. Among intervals covering the query, the **smallest size interval** is needed.
     - A **min-heap** is ideal:
       - Store intervals as `{end, start}`
       - Heap ordered by **interval size = end - start + 1**
       - `heap.peek()` always gives the smallest interval covering the query
     - Process queries in **sorted order** to efficiently add/remove intervals from the heap.

     Step-by-Step Thought Process:
     -----------------------------
     1. Sort queries by value while keeping original indices.
     2. Sort intervals by start time.
     3. Initialize a min-heap ordered by interval size (end - start + 1).
     4. For each query in sorted order:
        a) Add intervals to heap whose start <= query.
        b) Remove intervals from heap whose end < query (cannot cover it).
        c) If heap is empty → no interval covers query → result = -1
           Else → smallest interval = heap.peek() → result = interval.end - interval.start + 1

     Example Walkthrough:
     --------------------
     Intervals: [[1,4],[2,4],[3,6],[4,4]]
     Queries: [2,3,4,5]

     Query 1:
     - Add intervals with start <= 2 → [1,4], [2,4]
     - Remove intervals with end < 2 → none
     - Heap peek = [2,4] (size 3) → result[0] = 3

     Query 2:
     - Add intervals with start <= 3 → [3,6]
     - Remove intervals with end < 3 → none
     - Heap peek = [2,4] (size 3) → result[1] = 3

     Query 3:
     - Add intervals with start <= 4 → [4,4]
     - Remove intervals with end < 4 → none
     - Heap peek = [4,4] (size 1) → result[2] = 1

     Query 4:
     - Add intervals with start <= 5 → none
     - Remove intervals with end < 5 → [1,4], [2,4], [4,4] removed
     - Heap peek = [3,6] (size 4) → result[3] = 4

     Complexity Analysis:
     --------------------
     - Time: O(N log N + Q log N)
       - Sorting intervals: O(N log N)
       - Sorting queries: O(Q log Q)
       - Heap operations: O(log N) per add/remove → total O(N log N + Q log N)
     - Space: O(N)
       - Heap can contain up to N intervals in worst case
    */
    private static int[] minIntervalOptimized(int[][] intervals, int[] queries){
        int numberOfQueries = queries.length;
        int[] result = new int[numberOfQueries];

        int[][] sortedQueries = new int[numberOfQueries][2];
        for(int i=0; i<numberOfQueries; i++){
            sortedQueries[i][0] = queries[i];
            sortedQueries[i][1] = i;
        }
        Arrays.sort(sortedQueries, (a,b) -> a[0] - b[0]);
        Arrays.sort(intervals, (a,b) -> a[0] - b[0]);

        // Heap stores {end, start}, ordered by interval length
        PriorityQueue<int[]> heap = new PriorityQueue<>(
                (a, b) -> (a[0] - a[1]) - (b[0] - b[1])
        );

        int i=0;

        for(int[] q : sortedQueries){
            int query = q[0];
            int index = q[1];

            while(i < intervals.length && intervals[i][0] <= query){
                int start = intervals[i][0];
                int end = intervals[i][1];
                heap.offer(new int[]{end, start});
                i++;
            }

            while(!heap.isEmpty() && heap.peek()[0] < query){
                heap.poll();
            }

            if(heap.isEmpty()){
                result[index] = -1;
            }else{
                int[] interval = heap.peek();
                result[index] = interval[0] - interval[1] + 1;
            }

        }

        return result;
    }


}
