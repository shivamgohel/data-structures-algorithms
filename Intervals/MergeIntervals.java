// https://leetcode.com/problems/merge-intervals/description/

import java.util.*;

public class MergeIntervals {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given a collection of intervals, merge all overlapping intervals.

         Example 1:
         Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
         Output: [[1,6],[8,10],[15,18]]
         Explanation:
         - Intervals [1,3] and [2,6] overlap → merge to [1,6]
         - Intervals [8,10] and [15,18] do not overlap → keep as is
        */

        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};

        // Optimized approach → TC: O(n log n), SC: O(n)
        mergeOptimized(intervals);

    }

    /*
     Approach: Optimized Sorting + Merge
     -----------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - Intervals may overlap.
        - Merge overlapping intervals into a single interval.
        - Return the resulting intervals in sorted order by start time.

     2. Key Insight (Overlap vs Non-Overlap):
        - Sort intervals by start time first → ensures any overlapping intervals are consecutive.
        - last = last interval in merged list
        - current = current interval from sorted intervals

        a) Non-overlapping case:
           Condition: last[1] < current[0]
           Meaning: last interval ends before current interval starts → no overlap
           Action: add current interval to merged as-is.

           Example:
             merged = [[1,3]]
             current = [8,10]
             3 < 8 → non-overlapping → add
             merged → [[1,3],[8,10]]

        b) Overlapping case:
           Condition: last[1] >= current[0]
           Meaning: intervals overlap → need to merge
           Action: last[1] = max(last[1], current[1])
           Note: last[0] remains unchanged because intervals are sorted by start.

           Example:
             merged = [[1,3]]
             current = [2,6]
             3 >= 2 → overlapping → merge to [1,6]
             merged → [[1,6]]

     3. Algorithm:
        - Check if intervals is null or empty → return empty array
        - Sort intervals by start time
        - Initialize merged list
        - Loop through each interval:
            - If merged is empty or last[1] < current[0] → add interval
            - Else → overlapping → merge by updating last[1] = max(last[1], current[1])
        - Convert merged list to int[][] and return

     4. Dry Run Example:
        Input: [[1,3],[2,6],[8,10],[15,18]]

        Step 1: Sort intervals → [[1,3],[2,6],[8,10],[15,18]] (already sorted)
        Step 2: Initialize merged → []
        Step 3: Loop through intervals:
            interval = [1,3] → merged empty → add → merged = [[1,3]]
            interval = [2,6] → 3 >= 2 → overlap → merge → merged = [[1,6]]
            interval = [8,10] → 6 < 8 → no overlap → add → merged = [[1,6],[8,10]]
            interval = [15,18] → 10 < 15 → no overlap → add → merged = [[1,6],[8,10],[15,18]]

        Output: [[1,6],[8,10],[15,18]]

     5. Complexity Analysis:
        - Time Complexity: O(n log n) → sorting dominates
        - Space Complexity: O(n) → storing merged list
    */
    private static int[][] mergeOptimized(int[][] intervals){
        if(intervals == null || intervals.length == 0)
            return new int[0][0];

        Arrays.sort(intervals, (a,b) -> a[0]- b[0]);
        List<int[]> merged = new ArrayList<>();

        for(int[] interval : intervals){
            if(merged.isEmpty() || merged.get(merged.size()-1)[1] < interval[0] ){
                merged.add(interval);
            }
            else{
                merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], interval[1] );
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

}
