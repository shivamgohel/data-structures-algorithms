import java.util.Arrays;

public class MeetingRooms1 {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given a collection of meeting time intervals, determine if a person can attend all meetings.
         Each interval is represented as [start, end].

         Example:
         Input: intervals = [[0,30],[5,10],[15,20]]
         Output: false
         Explanation:
         - [0,30] overlaps with [5,10] → cannot attend all meetings
        */

        int[][] intervals = {{0,30},{5,10},{15,20}};

        // Brute Force 1 → Check all pairs TC: O(n^2)
        canAttendMeetingsBrute1(intervals);

        // Brute Force 2 → Use max(start) & min(end) logic TC: O(n^2)
        canAttendMeetingsBrute2(intervals);

        // Optimized → Sort by start time, check adjacent meetings TC: O(n log n)
        canAttendMeetingsOptimized1(intervals);


        /*
         FOLLOW-UP:
         Find the maximum number of non-overlapping meetings that a person can attend.
         - Idea: Sort intervals by end time
         - Pick earliest-ending meeting first (greedy)
         - Count all meetings where currentStart >= lastEnd

         Example:
         Input: intervals = [[0,30],[5,10],[15,20],[30,40]]
         Output: 3
         Explanation:
         - Pick [5,10], [15,20], [30,40] → maximum 3 non-overlapping meetings
        */
        // Max Meetings → Sort by end time, greedy approach TC: O(n log n)
        System.out.println(maxMeetings(intervals));
    }

    /*
     Approach 1: Brute Force → Check All Pairs
     -----------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Determine if a person can attend all meetings.
        - Overlaps between any two meetings make it impossible.

     2. Key Insight:
        - Two meetings [start1,end1] and [start2,end2] overlap if:
              start1 < end2 && start2 < end1
        - If any pair overlaps → return false.

     3. Algorithm:
        - Loop i = 0 → n-1:
            - Loop j = i+1 → n-1:
                - If intervals i and j overlap → return false
        - If no overlaps found → return true

     4. One-Line Memory Trick
        -------------------------
        **Overlap exists if each meeting starts before the other one ends.**

        Visual:
           Meeting A: |-----|
           Meeting B:     |-----|
           Overlap? Yes → cannot attend both

           Meeting A: |-----|
           Meeting B:           |-----|
           Overlap? No → can attend both

     5. Complexity Analysis:
        - Time: O(n^2) → all pairs compared
        - Space: O(1)
    */
    private static boolean canAttendMeetingsBrute1(int[][] intervals){
        for(int i=0; i<intervals.length; i++){
            for(int j=i+1; j<intervals.length; j++){
                int prevStart = intervals[i][0];
                int prevEnd = intervals[i][1];

                int currentStart = intervals[j][0];
                int currentEnd = intervals[j][1];

                if(currentStart < prevEnd && prevStart < currentEnd){
                    return false;
                }
            }
        }

        return true;
    }

    /*
     Approach 2: Brute Force → Max(Start) & Min(End)
     -----------------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Determine if a person can attend all meetings.
        - Any overlap between two meetings makes it impossible.

     2. Key Insight:
        - Two meetings overlap if the intersection has positive length.
        - Intersection boundaries are:
              overlapStart = max(start1, start2)
              overlapEnd   = min(end1, end2)
     ** - If overlapStart < overlapEnd: **
            → the meeting starts BEFORE the overlapping end time
            → meaning one meeting begins while the other is still running
            → therefore, an overlap exists.

     3. Algorithm:
        - Loop i = 0 → n-1:
            - Loop j = i+1 → n-1:
                - Compute overlapStart and overlapEnd
                - If overlapStart < overlapEnd → return false
        - If no overlaps found → return true

     4. One-Line Memory Trick
        -------------------------
        **If max(start) < min(end), the meetings overlap.**
          { one meeting starts before the other meeting ends }
          { one meeting begins while the other is still running }

        Visual:
           Meeting A: |---------|
           Meeting B:      |---------|
                           ↑ overlapStart
                                ↑ overlapEnd
           overlapStart < overlapEnd → overlap exists

           Meeting A: |--------|
           Meeting B:             |---------|
                                  ↑ overlapStart
                               ↑ overlapEnd
           overlapStart ≥ overlapEnd → no overlap

     5. Complexity Analysis:
        - Time: O(n^2) → all pairs checked
        - Space: O(1)
    */
    private static boolean canAttendMeetingsBrute2(int[][] intervals){
        for(int i=0; i<intervals.length; i++){
            for(int j=i+1; j< intervals.length; j++){
                int start1 = intervals[i][0];
                int end1 = intervals[i][1];

                int start2 = intervals[j][0];
                int end2 = intervals[j][1];

                int overlapStart = Math.max(start1, start2);
                int overlapEnd = Math.min(end1, end2);

                if(overlapStart < overlapEnd)
                    return false;
            }
        }

        return true;
    }

    /*
     Optimized Approach 1: Sort by Start Time → Check Adjacent Intervals
     ------------------------------------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Determine if a person can attend all meetings.
        - Any overlap between two meetings makes it impossible.

     2. Key Insight:
        - After sorting by start time, overlapping meetings become adjacent.
        - We only need to compare each meeting with the previous one.
        - If a meeting starts before the previous one ends → overlap exists.

     3. Algorithm:
        - Sort intervals by start time.
        - Initialize prevEnd = end of the first meeting.
        - Loop i = 1 → n-1:
            - If currentStart < prevEnd → overlap → return false
            - Else → update prevEnd = currentEnd
        - If no overlaps found → return true

     4. One-Line Memory Trick
        -------------------------
        After sorting by start time:
        If currentStart < previousEnd, one meeting starts before the other ends → overlap.

        Visual:
           Sorted by start:
           [----A----]
                 [----B----]
                 ↑ currentStart < previousEnd → overlap

           [----A----]
                         [----B----]
                         ↑ currentStart ≥ previousEnd → no overlap

     5. Complexity Analysis:
        - Time: O(n log n) → sorting dominates
        - Space: O(1)
    */
    private static boolean canAttendMeetingsOptimized1(int[][] intervals){
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);

        int prevEnd = intervals[0][1];
        for(int i=1; i<intervals.length; i++){
            int currentStart = intervals[i][0];

            if(currentStart < prevEnd)
                return false;

            prevEnd = intervals[i][1]; // update prevEnd to currentEnd
        }

        return true;
    }

    /*
     FOLLOW-UP: Maximum Number of Non-Overlapping Meetings
     ----------------------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - Find the maximum number of meetings that can be attended.
        - Only one meeting can be attended at a time.
        - Meetings must not overlap.

     2. Key Insight (Greedy Choice):
        - Always attend the meeting that ends the earliest.
        - An earlier ending meeting leaves more room for future meetings.
        - This greedy choice guarantees the maximum count.

     3. Algorithm:
        - Sort intervals by end time.
        - Initialize count = 1 (pick the first meeting).
        - Track prevEnd = end of the last selected meeting.
        - Loop i = 1 → n-1:
            - If currentStart >= prevEnd:
                - Attend the meeting
                - count++
                - update prevEnd = currentEnd
        - Return count

     5. Complexity Analysis:
        - Time: O(n log n) → sorting dominates
        - Space: O(1)
    */
    private static int maxMeetings(int[][] intervals){
        Arrays.sort(intervals, (a,b) -> a[1] - b[1]);

        int count = 1;
        int prevEnd = intervals[0][1];

        for(int i=1; i<intervals.length; i++){
            int currentStart = intervals[i][0];
            int currentEnd = intervals[i][1];

            if(currentStart >= prevEnd){
                count++;
                prevEnd = currentEnd;
            }
        }

        return count;
    }

}
