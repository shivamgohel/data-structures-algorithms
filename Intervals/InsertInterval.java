// https://leetcode.com/problems/insert-interval/description/

import java.util.*;

public class InsertInterval {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given a set of non-overlapping intervals sorted by start time,
         insert a new interval into the intervals (merge if necessary) and
         return the resulting intervals sorted by start time.

         Example:
         Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
         Output: [[1,5],[6,9]]

         Explanation:
         1. Insert [2,5] into the list: [[1,3],[2,5],[6,9]]
         2. Merge overlapping intervals:
            - [1,3] overlaps with [2,5] → merge to [1,5]
            - [6,9] does not overlap
         3. Result: [[1,5],[6,9]]
        */

        int[][] intervals = {{1,3},
                             {6,9}};

        int[] newInterval = {2,5};

        // Brute-force approach → TC: O(n log n), SC: O(n)
        insertBrute(intervals, newInterval);

        // Optimized approach → TC: O(n), SC: O(n)
        insertOptimized(intervals, newInterval);

    }

    /*
     Approach 1: Brute-force by sorting all intervals
     ------------------------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - We need to insert a new interval and merge overlapping intervals.
        - Overlapping intervals share some common range, and we must merge them
          into a single interval that spans both.
        - Non-overlapping intervals are completely separate and can remain as-is.

     2. Key Insight (Overlap vs Non-Overlap):
        - Let "last" = last interval in the merged list
        - Let "current" = current interval from the sorted list

        a) Non-Overlapping Case:
           Condition: last[1] < current[0]
           Meaning: The last merged interval ends **before** the current interval starts.
           Action: No overlap → we can safely add the current interval to merged as-is.

           Example:
             merged = [[1,3]]
             current = [6,9]
             last[1] = 3 < current[0] = 6 → non-overlapping → add [6,9]
             merged → [[1,3],[6,9]]

        b) Overlapping Case:
           Condition: last[1] >= current[0]
           Meaning: The last merged interval ends **at or after** the current interval starts.
           Action: Overlap → merge by updating last[1] = max(last[1], current[1]).
           Note: We do not update start because intervals are sorted by start, so
                 last[0] ≤ current[0] is guaranteed.

           Example:
             merged = [[1,3]]
             current = [2,5]
             last[1] = 3 >= current[0] = 2 → overlapping → merge to [1,5]
             merged → [[1,5]]

     3. Algorithm:
        - Convert intervals array to a list for dynamic addition.
        - Add newInterval to the list.
        - Sort the list by interval start time.
        - Initialize a merged list.
        - Loop through each interval:
            - If merged is empty or last[1] < current[0] → non-overlapping → add
            - Else → overlapping → merge by updating last[1] = max(last[1], current[1])

     4. Complexity Analysis:
        - Time Complexity: O(n log n) → due to sorting all intervals
        - Space Complexity: O(n) → storing all intervals in a list
    */
    private static int[][] insertBrute(int[][] intervals, int[] newInterval){
        List<int[]> allIntervals = new ArrayList<>(Arrays.asList(intervals));
        allIntervals.add(newInterval);

        allIntervals.sort((a,b) -> a[0] - b[0]);

        List<int[]> merged = new ArrayList<>();
        for(int[] interval : allIntervals){
            if(merged.isEmpty() || merged.get(merged.size()-1)[1] < interval[0] ){
                merged.add(interval);
            }else{
                merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1] , interval[1]);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

    /*
     Approach 2: Optimized Linear Scan (Single Pass)
     -----------------------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - Intervals are sorted and non-overlapping.
        - We want to insert a new interval and merge any overlapping intervals.
        - Because the list is sorted, we can do this in a single pass (O(n)).

     2. Key Insight (Why Single Pass Works):
        - Let "newInterval" = interval to insert
        - Loop through intervals with index `idx` and categorize each interval:

        a) Intervals Completely Before newInterval (Non-Overlapping - Left):
           Condition: intervals[idx][1] < newInterval[0]
           Meaning: Interval ends **before** newInterval starts → no overlap.
           Action: Add interval to result as-is.

        b) Intervals Overlapping with newInterval:
           Condition: intervals[idx][0] <= newInterval[1]
           Meaning: Interval starts **before or at the end** of newInterval → there is overlap.
           Why this works:
             - If current interval starts after newInterval ends, there is no overlap.
             - Otherwise, they share some range and need to be merged.
             - Because intervals are sorted, all consecutive overlapping intervals will satisfy this condition.
           Action: Merge by updating:
               - newInterval[0] = min(newInterval[0], interval[0])
               - newInterval[1] = max(newInterval[1], interval[1])

        c) Intervals Completely After newInterval (Non-Overlapping - Right):
           Condition: intervals[idx][0] > newInterval[1]
           Meaning: Interval starts **after** newInterval ends → no overlap.
           Action: Stop merging, add newInterval, then add the rest of intervals as-is.

     3. Algorithm:
        - Initialize empty result list `res`.
        - Step 1: Add all intervals completely before newInterval.
        - Step 2: Merge all overlapping intervals with newInterval.
        - Step 3: Add merged newInterval to result.
        - Step 4: Add all remaining intervals.

     4. Dry Run Example:

        Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]

        Step 1: Add intervals before newInterval (interval[1] < newInterval[0]):
            interval = [1,2], 2 < 4 → yes → add [1,2]
            res = [[1,2]]
            interval = [3,5], 5 < 4 → no → stop
            idx now at 1

        Step 2: Merge overlapping intervals (interval[0] <= newInterval[1]):
            interval = [3,5], 3 <= 8 → overlap → merge
                newInterval = [min(4,3), max(8,5)] = [3,8]
            interval = [6,7], 6 <= 8 → overlap → merge
                newInterval = [min(3,6), max(8,7)] = [3,8]
            interval = [8,10], 8 <= 8 → overlap → merge
                newInterval = [min(3,8), max(8,10)] = [3,10]
            interval = [12,16], 12 <= 10 → no → stop merging

        Step 3: Add merged newInterval:
            res = [[1,2],[3,10]]

        Step 4: Add remaining intervals:
            interval = [12,16] → add as-is
            res = [[1,2],[3,10],[12,16]]

        Output: [[1,2],[3,10],[12,16]]

     5. Complexity Analysis:
        - Time Complexity: O(n) → single pass through all intervals
        - Space Complexity: O(n) → storing result list
    */
    private static int[][] insertOptimized(int[][] intervals, int[] newInterval){
        List<int[]> res = new ArrayList<>();

        int idx = 0;
        while(idx < intervals.length && intervals[idx][1] < newInterval[0]){
            res.add(intervals[idx]);
            idx++;
        }

        while(idx < intervals.length && intervals[idx][0] <= newInterval[1]){
            newInterval[0] = Math.min(newInterval[0], intervals[idx][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[idx][1]);
            idx++;
        }
        res.add(newInterval);

        while(idx < intervals.length){
            res.add(intervals[idx]);
            idx++;
        }

        return res.toArray(new int[res.size()][]);
    }

}

