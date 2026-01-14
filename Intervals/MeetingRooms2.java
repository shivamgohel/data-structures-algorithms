import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class MeetingRooms2 {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given a collection of meeting time intervals,
         determine the **minimum number of meeting rooms** required to schedule all meetings.

         Example:
         Input: intervals = [[0,30],[5,10],[15,20]]
         Output: 2
         Explanation:
         - [0,30] and [5,10] overlap → need at least 2 rooms
         - [15,20] overlaps with [0,30] → still 2 rooms sufficient
        */

        int[][] intervals = {{0,30},{5,10},{15,20}};

        // Min-Heap approach → TC: O(n log n)  SC: O(n)
        System.out.println(minMeetingRoomsOptimized1(intervals));

        // Line sweep (TreeMap) → TC: O(n log n)  SC: O(n)
        System.out.println(minMeetingRoomsOptimized2(intervals));

        // Two-pointer / greedy → TC: O(n log n)  SC: O(n)
        System.out.println(minMeetingRoomsOptimized3(intervals));

    }

    /*
     Approach 1: Min-Heap (Greedy)
     -----------------------------
     Problem:
     - Given meeting intervals, we want the **minimum number of meeting rooms** required.
     - A person can have multiple meetings in parallel, but a room can hold only one meeting at a time.

     Intuition / Why Heap:
     ---------------------
     - We need to know **when a room becomes free** to reuse it for the next meeting.
     - If a meeting starts after the earliest ending meeting → we can reuse that room.
     - If a meeting starts before the earliest ending meeting → we need a **new room**.
     - The natural data structure for “earliest ending meeting” is a **min-heap** (priority queue):
         - `heap.peek()` always gives the earliest end time.
         - `heap.poll()` removes the meeting that has ended → room freed.
     - This way, the heap always contains the **end times of meetings currently occupying rooms**.
     - The **size of the heap at any moment** is the number of rooms in use at that time.
     - At the end, the **maximum heap size** = minimum rooms required.

     Step-by-Step Thought Process:
     -----------------------------
     1. **Sort the meetings by start time**:
        - This ensures we handle meetings in chronological order.
        - Sorting helps us process one meeting at a time and decide if a room is free.

     2. **Initialize a min-heap**:
        - Heap stores the **end times** of meetings currently occupying rooms.
        - At any time, the top of the heap (`heap.peek()`) tells us the **earliest ending meeting**.

     3. **Iterate through each meeting**:
        - `start` = meeting start time
        - `end` = meeting end time

        a) If `start >= heap.peek()`:
           - Earliest meeting has ended → room is free.
           - Remove the earliest ending meeting from the heap (`heap.poll()`).
           - This simulates **reusing the same room**.

        b) Add the current meeting’s end time to the heap (`heap.add(end)`):
           - Whether we reused a room or allocated a new one, we now occupy a room until `end`.
           - Heap size now = current rooms in use.

     4. **Result**:
        - After processing all meetings, the **size of the heap** = minimum number of rooms required.

     Example Walkthrough:
     --------------------
     Input: [[0,30], [5,10], [15,20]]

     Step 1: Sort → [[0,30], [5,10], [15,20]]
     Heap initially empty

     Iteration 1: [0,30]
     - Heap empty → add 30 → Heap = [30]

     Iteration 2: [5,10]
     - 5 < 30 → overlap → need new room → add 10 → Heap = [10,30]

     Iteration 3: [15,20]
     - 15 >= 10 → room freed → remove 10 → Heap = [30]
     - Add 20 → Heap = [20,30]

     Heap size = 2 → minimum 2 rooms needed

     Complexity Analysis:
     --------------------
     - Time: O(n log n)
       - Sorting: O(n log n)
       - Heap operations: O(log n) per meeting → total O(n log n)
     - Space: O(n)
       - Heap can contain up to n meetings in the worst case
    */
    private static int minMeetingRoomsOptimized1(int[][] intervals){
        if(intervals == null || intervals.length ==0 )
            return 0;

        Arrays.sort(intervals, (a,b) -> a[0] - b[0]);
        // use -> (intervals, (a,b) -> Integer.compare(a[0], b[0]) )

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for(int i=0; i< intervals.length; i++){
            int start = intervals[i][0];
            int end = intervals[i][1];

            if(!minHeap.isEmpty() && start >= minHeap.peek()){
                minHeap.poll();
            }

            minHeap.add(end);
        }

        return minHeap.size();
    }

    /*
     Approach 2: Line Sweep (TreeMap)
     --------------------------------
     Problem:
     - Given meeting intervals, find the **minimum number of meeting rooms** required.
     - Idea: Track how the number of ongoing meetings changes over time.

     Intuition / Why TreeMap:
     ------------------------
     - Each meeting has a **start** and an **end**.
     - When a meeting starts → we need a room (+1)
     - When a meeting ends → room becomes free (-1)
     - If we track all these changes along a timeline, the **maximum number of rooms in use at any moment** is the answer.
     - A TreeMap is ideal because:
         - Keys = time points (start or end times)
         - Values = net change in rooms at that time
             - start → +1
             - end → -1
         - TreeMap keeps keys **automatically sorted**, allowing a natural "sweep" of time.

     Step-by-Step Thought Process:
     -----------------------------
     1. **Create a TreeMap<Integer, Integer> timeline**:
        - Key = time point (meeting start or end)
        - Value = net change in rooms at that time

     2. **Populate the timeline**:
        - For each interval [start, end]:
            - timeline[start] += 1 → a room is needed at meeting start
            - timeline[end] -= 1 → a room is freed at meeting end

     3. **Sweep through timeline in order**:
        - Initialize `currentRoomsInUse = 0` → rooms currently occupied
        - Initialize `maxRoomsNeeded = 0` → maximum rooms required
        - For each `roomDelta` in timeline.values() (chronologically):
            - `currentRoomsInUse += roomDelta` → update rooms in use
            - `maxRoomsNeeded = Math.max(maxRoomsNeeded, currentRoomsInUse)`

     4. **Result**:
        - After sweeping all time points, `maxRoomsNeeded` = minimum number of meeting rooms required

     Example Walkthrough:
     --------------------
     Input: [[0,30], [5,10], [15,20]]

     Step 1: Build timeline
     - 0 → +1 (meeting [0,30] starts)
     - 30 → -1 (meeting [0,30] ends)
     - 5 → +1 (meeting [5,10] starts)
     - 10 → -1 (meeting [5,10] ends)
     - 15 → +1 (meeting [15,20] starts)
     - 20 → -1 (meeting [15,20] ends)

     Timeline after aggregation and sorting:
     0: +1
     5: +1
     10: -1
     15: +1
     20: -1
     30: -1

     Step 2: Sweep timeline
     - time 0 → currentRoomsInUse = 1 → maxRoomsNeeded = 1
     - time 5 → currentRoomsInUse = 2 → maxRoomsNeeded = 2
     - time 10 → currentRoomsInUse = 1 → maxRoomsNeeded = 2
     - time 15 → currentRoomsInUse = 2 → maxRoomsNeeded = 2
     - time 20 → currentRoomsInUse = 1 → maxRoomsNeeded = 2
     - time 30 → currentRoomsInUse = 0 → maxRoomsNeeded = 2

     Answer: 2 rooms required

     Advantages of TreeMap:
     ----------------------
     - Automatically keeps the time points sorted
     - Handles multiple meetings starting or ending at the same time
     - Easy to implement line sweep without manually sorting start/end arrays

     Complexity Analysis:
     --------------------
     - Time: O(n log n)
         - TreeMap insertion: O(log n) per start/end → 2n insertions → O(n log n)
         - Sweeping timeline: O(n)
     - Space: O(n)
         - TreeMap stores up to 2n time points
    */
    private static int minMeetingRoomsOptimized2(int[][] intervals){
        if(intervals == null || intervals.length == 0)
            return 0;

        TreeMap<Integer, Integer> timeline = new TreeMap<>();
        for(int[] interval : intervals){
            timeline.put(interval[0], timeline.getOrDefault(interval[0], 0) +1);
            timeline.put(interval[1], timeline.getOrDefault(interval[1], 0) -1);
        }

        int currentRoomsInUse = 0;
        int maxRoomsNeeded = 0;

        for(int roomDelta : timeline.values()){
            currentRoomsInUse += roomDelta;
            maxRoomsNeeded = Math.max(maxRoomsNeeded, currentRoomsInUse );
        }

        return maxRoomsNeeded;
    }

    /*
     Approach 3: Two-Pointer / Greedy
     --------------------------------
     Problem:
     - Given meeting intervals, find the **minimum number of meeting rooms** required.
     - Idea: Track rooms in use by comparing **meeting start times** and **end times** in order.

     Intuition / Why Two-Pointers:
     -----------------------------
     - Each meeting occupies a room from start to end.
     - If a meeting starts after another meeting ends → room can be **reused**.
     - To efficiently check this:
         1. Sort all **start times** in ascending order.
         2. Sort all **end times** in ascending order.
     - Use two pointers:
         - `left` → points to the next meeting to start
         - `right` → points to the next meeting to end
     - Compare `startTimes[left]` with `endTimes[right]`:
         - If `startTimes[left] < endTimes[right]` → overlap → need new room
         - Else → no overlap → room freed → move `right` pointer

     Step-by-Step Thought Process:
     -----------------------------
     1. **Extract start and end times**:
        - `startTimes[i] = intervals[i][0]`
        - `endTimes[i] = intervals[i][1]`

     2. **Sort startTimes and endTimes**:
        - Sorting allows us to process events in chronological order
        - Guarantees we always know the earliest starting and ending meeting

     3. **Initialize variables**:
        - `currentRoomsInUse = 0` → number of rooms occupied at the current time
        - `maxRoomsNeeded = 0` → maximum rooms needed at any moment
        - `left = 0` → pointer for startTimes
        - `right = 0` → pointer for endTimes

     4. **Iterate over meetings using two pointers**:
        - While `left < intervals.length`:
            a) If `startTimes[left] < endTimes[right]`:
               - Next meeting starts **before the earliest meeting ends** → room needed
               - `currentRoomsInUse++`
               - Move `left++` to next meeting
            b) Else:
               - Earliest meeting ended → room freed
               - `currentRoomsInUse--`
               - Move `right++` to next ending meeting
            - Update `maxRoomsNeeded = Math.max(maxRoomsNeeded, currentRoomsInUse)`

     5. **Result**:
        - After processing all meetings, `maxRoomsNeeded` = minimum number of rooms required

     Example Walkthrough:
     --------------------
     Input: [[0,30], [5,10], [15,20]]

     Step 1: Extract start and end times
     startTimes = [0, 5, 15]
     endTimes   = [10, 20, 30]

     Step 2: Sort (already sorted here)
     startTimes = [0, 5, 15]
     endTimes   = [10, 20, 30]

     Step 3: Initialize
     currentRoomsInUse = 0
     maxRoomsNeeded = 0
     left = 0, right = 0

     Step 4: Iterate
     - startTimes[0] = 0 < endTimes[0] = 10 → need room → currentRoomsInUse = 1 → maxRoomsNeeded = 1 → left++
     - startTimes[1] = 5 < endTimes[0] = 10 → need room → currentRoomsInUse = 2 → maxRoomsNeeded = 2 → left++
     - startTimes[2] = 15 >= endTimes[0] = 10 → room freed → currentRoomsInUse = 1 → right++
     - startTimes[2] = 15 < endTimes[1] = 20 → need room → currentRoomsInUse = 2 → maxRoomsNeeded = 2 → left++

     Step 5: Done → maxRoomsNeeded = 2

     Advantages of Two-Pointer Approach:
     -----------------------------------
     - No heap or TreeMap needed → simpler implementation
     - Efficient O(n log n) time → only sorting dominates
     - Space O(n) → for startTimes and endTimes arrays
     - Very intuitive once start and end times are separated

     Complexity Analysis:
     --------------------
     - Time: O(n log n)
         - Sorting startTimes and endTimes: O(n log n)
         - Iterating with two pointers: O(n)
     - Space: O(n)
         - Arrays startTimes and endTimes store n elements each
    */
    private static int minMeetingRoomsOptimized3(int[][] intervals){
        int[] startTimes = new int[intervals.length];
        int[] endTimes = new int[intervals.length];

        for(int i=0; i< intervals.length; i++){
            startTimes[i] = intervals[i][0];
            endTimes[i] = intervals[i][1];
        }

        Arrays.sort(startTimes);
        Arrays.sort(endTimes);

        int currentRoomsInUse = 0;
        int maxRoomsNeeded = 0;

        int left = 0;
        int right = 0;

        while(left < intervals.length){
            if(startTimes[left] < endTimes[right]){
                currentRoomsInUse++;
                left++;
            }
            else{
                currentRoomsInUse--;
                right++;
            }

            maxRoomsNeeded = Math.max(maxRoomsNeeded, currentRoomsInUse);
        }

        return maxRoomsNeeded;
    }

}
