// https://leetcode.com/problems/happy-number/description/

import java.util.HashSet;
import java.util.Set;

public class HappyNumber {
    public static void main(String[] args) {

        /*
         Problem Statement:
         A number is called a happy number if the following process ends in 1:
         - Starting with any positive integer, replace the number by the sum of the
           squares of its digits.
         - Repeat the process until the number equals 1 (happy),
           or it loops endlessly in a cycle (unhappy).

         Example 1:
         Input: n = 19
         Output: true

         Explanation:
         1² + 9² = 82
         8² + 2² = 68
         6² + 8² = 100
         1² + 0² + 0² = 1
        */

        int n = 2;

        // Brute-force approach (HashSet) → TC: O(k), SC: O(k)
        System.out.println(isHappyBrute(n));

        // Optimized approach (Floyd’s Cycle Detection) → TC: O(k), SC: O(1)
        System.out.println(isHappyOptimized1(n));

        // Optimized approach (Floyd’s Cycle Detection – offset fast pointer) → TC: O(k), SC: O(1)
        System.out.println(isHappyOptimized2(n));

    }

    /*
     Approach 1: Brute-force using HashSet
     ------------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - We repeatedly replace a number with the sum of the squares of its digits.
        - Two outcomes are possible:
            a) The number eventually becomes 1 → happy number
            b) The number enters a cycle → unhappy number

     2. Key Observation:
        - If a number repeats, we are stuck in a cycle.
        - We can detect cycles by storing previously seen numbers.

     3. Algorithm:
        - Create a HashSet to store visited numbers.
        - While n is not equal to 1:
            - If n is already in the set, return false (cycle detected).
            - Add n to the set.
            - Replace n with the sum of squares of its digits.

     4. Complexity Analysis:
        - Time Complexity: O(k), where k is the number of generated values.
        - Space Complexity: O(k), due to HashSet usage.
    */
    private static boolean isHappyBrute(int n){
        Set<Integer> seen = new HashSet<>();

        while(n != 1){
            if(seen.contains(n)){
                return false;
            }

            seen.add(n);
            n = getNext(n);
        }

        return true;
    }
    private static int getNext(int n){
        int res = 0;

        while(n > 0){
            int lastDigit = n % 10;
            res += (lastDigit * lastDigit);

            n /= 10;
        }

        return res;
    }

    /*
     Approach 2: Optimized using Floyd’s Cycle Detection (Slow & Fast Pointers)
     --------------------------------------------------------------------------
     Idea and Thought Process:

     1. Key Insight:
        - The sequence of numbers behaves like a linked list.
        - Happy numbers end at 1.
        - Unhappy numbers fall into a cycle.

     2. Floyd’s Algorithm:
        - Use two pointers:
            - slow moves one step at a time
            - fast moves two steps at a time
        - If there is a cycle, slow and fast will meet.

     3. Algorithm:
        - Initialize slow and fast to n.
        - Loop indefinitely:
            - Move slow one step.
            - Move fast two steps.
            - If fast becomes 1 → happy number.
            - If slow == fast → cycle detected → unhappy number.

     4. Complexity Analysis:
        - Time Complexity: O(k)
        - Space Complexity: O(1)
    */
    private static boolean isHappyOptimized1(int n){
        int slow = n;
        int fast = n;

        while(true){
            slow = getNext(slow);
            fast = getNext(fast);
            fast = getNext(fast);

            if(fast == 1){
                return true;
            }

            if(slow == fast){
                return false;
            }
        }
    }

    /*
     Approach 3: Optimized Floyd’s Algorithm (Cleaner Variant)
     ---------------------------------------------------------
     Idea and Thought Process:

     1. Improvement:
        - Initialize the fast pointer one step ahead to avoid the initial
          equality between slow and fast.

     2. Algorithm:
        - slow starts at n.
        - fast starts at getNext(n).
        - Continue looping while:
            - fast is not equal to 1 (we have not reached a happy number), and
            - slow is not equal to fast (no cycle detected yet).
        - Move slow one step at a time.
        - Move fast two steps at a time.

     3. Loop Termination Conditions:
        - The loop ends in one of two cases:
            a) fast == 1
               → The sequence reaches 1, so the number is a happy number.
            b) slow == fast
               → A cycle is detected, so the number is not a happy number.

     4. Return Statement:
        - After the loop:
            - If fast == 1, return true (happy number).
            - Otherwise, return false (cycle detected).

     5. Complexity Analysis:
        - Time Complexity: O(k)
        - Space Complexity: O(1)
    */
    private static boolean isHappyOptimized2(int n){
        int slow = n;
        int fast = getNext(n);

        while(fast != 1 && slow != fast){
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        }

        return fast == 1;
    }

}
