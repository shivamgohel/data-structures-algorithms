// https://leetcode.com/problems/non-overlapping-intervals/description/

import java.util.Arrays;

public class NonOverlappingIntervals {
    public void main(String[] args) {

        /*
         Problem Statement:
         Given a collection of intervals, find the **minimum number of intervals to remove**
         so that the remaining intervals are non-overlapping.

         Example:
         Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
         Output: 1
         Explanation:
         - Remove [1,3] → remaining intervals [[1,2],[2,3],[3,4]] are non-overlapping
        */

        int[][] intervals = {{1,2},{2,3},{3,4},{1,3}};

        // Optimized approach 1 → Sort by start time, keep interval with smaller end TC: O(n log n)
        System.out.println(eraseOverlapIntervalsOptimized1(intervals));

        // Optimized approach 2 → Sort by end time, greedy approach TC: O(n log n)
        System.out.println(eraseOverlapIntervalsOptimized2(intervals));
    }

    /*
     Approach 1: Sort by Start Time + Merge Overlaps
     ------------------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Remove minimum intervals to make the rest non-overlapping.
        - Intervals may overlap partially or completely.
        - Goal: maximize number of intervals kept → minimize removals.

     2. Key Insight:
        - Sort intervals by start time → overlapping intervals are consecutive.
        - Track `lastIntervalEnd` (end of the last interval we keep).
        - If current interval overlaps:
            - Remove one interval.
            - Keep the interval with **smaller end** to allow more future intervals.

     3. Algorithm:
        - Sort intervals by start.
        - Initialize `removals = 0` and `lastIntervalEnd = intervals[0][1]`.
        - Loop through intervals i = 1 → n-1:
            a) If currentStart >= lastIntervalEnd → no overlap → update lastIntervalEnd
            b) Else → overlap → removals++ → lastIntervalEnd = min(lastIntervalEnd, currentEnd)
        - Return removals

     4. Complexity Analysis:
        - Time: O(n log n) → sorting dominates
        - Space: O(1)
    */
    private static int eraseOverlapIntervalsOptimized1(int[][] intervals){
        if(intervals == null || intervals.length <= 1)
            return 0;

        Arrays.sort(intervals, (a,b) -> a[0] - b[0]);

        int removals = 0;
        int lastIntervalEnd = intervals[0][1];

        for(int i=1; i<intervals.length; i++){
            int currentStart = intervals[i][0];
            int currentEnd = intervals[i][1];

            if(currentStart >= lastIntervalEnd){
                lastIntervalEnd = currentEnd;
            }else{
                removals++;
                lastIntervalEnd = Math.min(lastIntervalEnd, currentEnd);
            }
        }

        return removals;
    }

    /*
     Approach 2: Sort by End Time (Greedy)
     -------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Same as above, but greedy strategy makes it simpler.
        - Always keep the interval that ends earliest to maximize space for next intervals.

     2. Key Insight:
        - Sort intervals by end time.
        - Track `lastIntervalEnd` (end of last interval we keep).
        - Loop through intervals:
            a) If currentStart >= lastIntervalEnd → no overlap → keep it and update lastIntervalEnd
            b) Else → overlap → increment removals

     3. Important Note:
        - **We do NOT update `lastIntervalEnd` on overlap**, because:
          - The previous interval ends earlier than the current interval (because we sorted by end time).
          - Keeping the previous interval is always optimal → allows more room for future intervals.
          - Updating it to the current interval’s end would potentially block more future intervals.
          - This is the key difference vs start-time sorting approach.

     4. Algorithm:
        - Sort intervals by end time.
        - Initialize `removals = 0` and `lastIntervalEnd = intervals[0][1]`.
        - Loop i = 1 → n-1:
            a) If currentStart >= lastIntervalEnd → keep → update lastIntervalEnd
            b) Else → removals++
        - Return removals

     5. Complexity Analysis:
        - Time: O(n log n) → sorting
        - Space: O(1)
    */
    private static int eraseOverlapIntervalsOptimized2(int[][] intervals){
        if(intervals == null || intervals.length <= 1)
            return 0;

        Arrays.sort(intervals, (a,b) -> a[1] - b[1]);

        int removals = 0;
        int lastIntervalEnd = intervals[0][1];

        for(int i=1; i<intervals.length; i++){
            int currentStart = intervals[i][0];
            int currentEnd = intervals[i][1];

            if(currentStart >= lastIntervalEnd){
                lastIntervalEnd = currentEnd;
            }else{
                removals++;
            }
        }

        return removals;
    }

}
