// https://leetcode.com/problems/task-scheduler/description/

import java.util.*;

public class TaskScheduler {
    public static void main(String[] args) {

        /**
         * Problem Statement:
         * -----------------
         * Given a characters array `tasks`, representing the tasks a CPU needs to do, where
         * each letter represents a different task. Tasks could be done in any order.
         * Each task is done in one unit of time.
         * * However, there is a cooldown period `n` between two same tasks. There must be at
         * least `n` units of time between any two same tasks.
         * * Goal: Return the least number of units of time to finish all tasks.
         */

        char[] tasks = {'A','A','A','B','B','B'};
        int n = 2;

        // Approach 1: Max-Heap Simulation -> TC: O(TotalTime * log 26), SC: O(1)
        System.out.println(leastIntervalOptimized1(tasks, n));

        // Approach 2: Idle Slot Filling -> TC: O(n), SC: O(1)
        System.out.println(leastIntervalOptimized2(tasks, n));

        // Approach 3: Math Formula (Optimized) -> TC: O(n), SC: O(1)
        System.out.println(leastIntervalOptimized3(tasks, n));

    }

    /*
      Approach 1: Max-Heap Simulation (Tick-by-Tick)
      ----------------------------------------------
      Intuition:
      - To minimize idle time, we must finish high-frequency tasks as soon as their
        cooldown allows.
      - A Max-Heap ensures we always pick the task that is most "urgent" (highest count).
      - A 'waitList' acts as a temporary holding area for tasks that have been executed
        but are still cooling down and cannot be picked yet.

      Step-by-Step Thought Process:
      -----------------------------
      1. Frequency Map: Count occurrences of each task.
      2. Priority Queue: Push all frequencies > 0 into a Max-Heap.
      3. CPU Cycles: Execute tasks in rounds of length (n + 1).
         a. In each round, try to pick (n + 1) distinct tasks from the heap.
         b. If a task is picked, decrement its frequency. If frequency > 0,
            add it to a temporary 'waitList'.
         c. If the heap becomes empty before the round ends but we still have
            tasks cooling down in the 'waitList', increment time (CPU is IDLE).
      4. Reset: After the round, push tasks from 'waitList' back into the Max-Heap.

      Dry Run (tasks = [A,A,A,B,B], n = 2):
      -------------------------------------
      Initial Heap: [A:3, B:2], n+1 = 3 (Round Size)

      Round 1:
      - Pick A: Time 1, Heap [B:2], waitList [A:2]
      - Pick B: Time 2, Heap [],    waitList [A:2, B:1]
      - Heap Empty? Yes. waitList Empty? No. -> CPU IDLE: Time 3.
      - End of Round: Re-add waitList. Heap: [A:2, B:1]

      Round 2:
      - Pick A: Time 4, Heap [B:1], waitList [A:1]
      - Pick B: Time 5, Heap [],    waitList [A:1]
      - Heap Empty? Yes. waitList Empty? No. -> CPU IDLE: Time 6.
      - End of Round: Re-add waitList. Heap: [A:1]

      Round 3:
      - Pick A: Time 7, Heap [],    waitList []
      - Heap Empty? Yes. waitList Empty? Yes. -> EXIT Loop.
      Total Time: 7.

      Complexity Analysis:
      --------------------
      - Time: O(TotalTime * log 26) -> We simulate every second of execution.
      - Space: O(1) -> Max-Heap size is capped at 26 (alphabet size).
    */
    private static int leastIntervalOptimized1(char[] tasks, int n){
        int[] freqs = new int[26];
        for(char c : tasks)
            freqs[c - 'A']++;

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for(int freq : freqs){
            if(freq > 0)
                maxHeap.offer(freq);
        }

        int time = 0;
        while(!maxHeap.isEmpty()){
            List<Integer> waitList = new ArrayList<>();
            int cycle = n + 1;

            for(int i=0; i<cycle; i++){
                if(!maxHeap.isEmpty()){
                    int currFreq = maxHeap.poll();
                    if(currFreq > 1)
                        waitList.add(currFreq - 1);
                }
                time++;

                if(maxHeap.isEmpty() && waitList.isEmpty())
                    break;
            }

            for(int remaining : waitList)
                maxHeap.add(remaining);
        }

        return time;
    }

    /*
      Approach 2: Idle Slot Reduction (Greedy Gap-Filling)
      --------------------------------------------------
      Intuition:
      - We identify the "bottleneck" (the most frequent task). This task dictates
        the minimum length of the schedule because of its mandatory cooldowns.
      - We build a "skeleton" using this task and treat the cooldown periods as
        empty "idle slots."
      - We then greedily "fill" these idle slots with all other available tasks
        to reduce the forced downtime.

      Step-by-Step Thought Process:
      -----------------------------
      1. Count: Find the frequency of each task.
      2. Sort: Determine which task is most frequent (maxFreq).
      3. Frame: Calculate total gaps between the most frequent tasks: (maxFreq - 1).
      4. Idle: Calculate initial idle slots: (totalGaps * n).
      5. Fill: Iterate through other tasks (from second-most frequent downwards):
         - Each task can fill at most (maxFreq - 1) idle slots.
         - Why? Because if a task has the same frequency as the max, its last
           instance will sit after the final max-task, not inside a gap.
      6. Result: If idle slots remain, the answer is (Total Tasks + Idle Slots).
         If idle slots are overfilled (negative), the answer is just (Total Tasks).

      Dry Run (tasks = [A,A,A,B,B,B], n = 2):
      ---------------------------------------
      1. Frequencies: A:3, B:3.
      2. maxFreq: 3 (Task A).
      3. Gaps: (3 - 1) = 2 gaps.
      4. Initial Idle Slots: 2 gaps * 2 (n) = 4.
         Visual: A _ _ A _ _ A  (The '_' are the 4 idle slots)

      5. Process Task B (freq 3):
         - Slots to fill: Math.min(maxFreq - 1, freqB) = Math.min(2, 3) = 2.
         - Update idleSlots: 4 - 2 = 2.
         - Visual: A B _ A B _ A (The 3rd 'B' is placed after the final 'A')

      6. Final Calculation:
         - tasks.length (6) + max(0, idleSlots) (2) = 8.

      Complexity Analysis:
      --------------------
      - Time: O(N) -> One linear pass to count tasks; sorting 26 elements is O(1).
      - Space: O(1) -> We only use a fixed-size frequency array of 26.
    */
    private static int leastIntervalOptimized2(char[] tasks, int n){
        int[] freqs = new int[26];
        for(char c : tasks)
            freqs[c - 'A']++;

        Arrays.sort(freqs);
        int maxFreq = freqs[25];

        int totalGaps = maxFreq - 1;
        int idleSlots = totalGaps * n;

        for(int i=24; i>=0 && freqs[i] > 0; i--){
            idleSlots -= Math.min(maxFreq-1, freqs[i]);
        }

        int remainingIdleSlots = Math.max(0, idleSlots);
        return tasks.length + remainingIdleSlots;
    }

    /*
      Approach 3: Mathematical Formula (Block & Tail)
      ----------------------------------------------
      Intuition:
      - This approach treats the schedule as a series of repeating "Execution Blocks."
      - The most frequent task (maxFreq) defines how many blocks we need.
      - Each block has a size of (n + 1), consisting of the task itself plus its
        required cooling slots.
      - The final occurrences of the most frequent tasks form a "Tail" that
        doesn't need cooling slots after them.

      Step-by-Step Thought Process:
      -----------------------------
      1. Find Max Frequency: Identify the highest count (maxFreq) of any task.
      2. Count Max Tasks: Find how many different tasks share that maxFreq (maxFreqCount).
      3. Calculate Body: (maxFreq - 1) * (n + 1).
         - (maxFreq - 1) is the number of full rounds.
         - (n + 1) is the width of each round (Task + Cooldown).
      4. Add Tail: Add 'maxFreqCount' to the result. These are the tasks that
         sit at the very end of the schedule.
      5. Final Guard: Return Math.max(tasks.length, calculatedTime).
         - If we have many unique tasks, we might not need any idle time,
           making tasks.length the actual answer.

      Dry Run (tasks = [A,A,A,B,B,B], n = 2):
      ---------------------------------------
      1. Count: A:3, B:3.
      2. maxFreq: 3.
      3. maxFreqCount: 2 (A and B both appear 3 times).

      4. Execution Blocks:
         - coolingRounds (maxFreq - 1) = 2.
         - roundLength (n + 1) = 3.
         - Body time = 2 * 3 = 6.
         - Visual: [A B _ ] [A B _ ]  <-- 2 blocks of size 3

      5. Add Tail:
         - tailLength (maxFreqCount) = 2.
         - Visual: [A B _ ] [A B _ ] A B

      6. Final Result: 6 + 2 = 8.

      Complexity Analysis:
      --------------------
      - Time: O(N) -> Single linear pass to count frequencies.
      - Space: O(1) -> Only uses a fixed-size array of 26.
    */
    private static int leastIntervalOptimized3(char[] tasks, int n){
        int[] freqs = new int[26];
        int maxFreq = 0;
        for(char c : tasks){
            freqs[c - 'A']++;
            maxFreq = Math.max(maxFreq, freqs[c - 'A']);
        }

        int maxFreqCount = 0;
        for(int x : freqs){
            if(x == maxFreq)
                maxFreqCount++;
        }

        int time = (maxFreq-1) * (n+1) + maxFreqCount;
        // maxFreq - 1  --> totalGaps
        // n + 1        --> cookingPeriod (1 task + N more tasks)
        // (maxFreq-1) * (n+1) --> IdleSlots
        // maxFreqCount --> final instances of the most frequent tasks
        return Math.max(tasks.length, time);
    }

}
