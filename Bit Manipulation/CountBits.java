// https://leetcode.com/problems/counting-bits/

public class CountBits {
    public static void main(String[] args){

        /*
         Problem Statement:
         You are given an integer n.
         Return an array ans of length n + 1 where:
         ans[i] is the number of '1' bits in the binary representation of i
        */

        int n = 5;

        // Brute-force: check all 32 bits for each number, TC: O(n * 32)
        countBitsBrute(n);

        // Better1: Brian Kernighan’s Algorithm, remove one set bit at a time, TC: O(total # of 1-bits)
        countBitsBetter1(n);

        // Better2: Using Java built-in Integer.bitCount(), TC: O(n)
        countBitsBetter2(n);

        // Optimized1: DP using odd/even logic, TC: O(n)
        countBitsOptimized1(n);

        // Optimized2: DP using right shift and last-bit check, TC: O(n)
        countBitsOptimized2(n);
    }

    /*
     Brute-force Approach:
     - For each number from 0 to n, check every bit position.
     - Since integers are 32-bit, iterate exactly 32 times per number.

     Steps:
     1. Create an array to store results.
     2. For each number:
        - Loop through all 32 bits.
        - Check if the bit is set using (num & (1 << i)).
        - Increment count if bit is 1.

     Time Complexity: O(n * 32) → effectively O(n)
     Space Complexity: O(n)
    */
    private static int[] countBitsBrute(int n){
        int[] res = new int[n+1];

        for(int num=0; num<=n; num++){
            for(int i=0; i<32; i++){
                if((num & (1 << i)) != 0){
                    res[num]++;
                }
            }
        }

        return res;
    }

    /*
     Better Approach:
     - Apply Brian Kernighan’s Algorithm to each number.
     - n & (n - 1) removes the lowest set bit.

     Steps:
     1. Loop from 0 to n.
     2. For each number:
        - Repeatedly remove the lowest set bit.
        - Count how many times this operation occurs.

     Why It Works:
     - Each iteration removes exactly one '1' bit.

     Time Complexity: O(total number of 1-bits)
     Space Complexity: O(n)
    */
    private static int[] countBitsBetter1(int n){
        int[] res = new int[n+1];

        for(int i=0; i<=n; i++){
            int num = i;
            while(num != 0){
                res[i]++;
                num = num & num-1;
            }
        }

        return res;
    }

    /*
     Better Approach 2 (In-built function):
     - Use Java's Integer.bitCount() to count 1-bits for each number.

     Steps:
     1. Loop from 0 to n.
     2. For each number, use Integer.bitCount(i) to get 1-bits count.

     Time Complexity: O(n)
     Space Complexity: O(n)
    */
    private static int[] countBitsBetter2(int n){
        int[] res = new int[n+1];

        for(int i=0; i<=n; i++){
            res[i] = Integer.bitCount(i);
        }

        return res;
    }

    /*
     Optimized Approach 1 (DP using odd/even logic):

     Key Idea:
     - Use previously computed results (dynamic programming) to calculate number of 1s in binary for i.
     - Observe the pattern:
        1. Even numbers: last bit is 0 → number of 1s same as i/2.
        2. Odd numbers: last bit is 1 → number of 1s = dp[i-1] + 1.

     Explanation:

     1. Even numbers:
        - Binary representation ends with 0 (LSB = 0).
        - Right shift by 1 (i/2) removes the last 0 bit.
        - Number of 1s in i is the same as in i/2.
        - Example:
            i = 4 → binary 100 → even
            i/2 = 2 → binary 10
            dp[2] = 1 → dp[4] = 1 (same number of 1s)

     2. Odd numbers:
        - Binary representation ends with 1 (LSB = 1).
        - Removing the last bit gives an even number i-1.
        - Number of 1s in i = number of 1s in i-1 + 1 (for the last 1-bit).
        - Example:
            i = 5 → binary 101 → odd
            i-1 = 4 → binary 100
            dp[4] = 1 → dp[5] = 1 + 1 = 2

     Steps:
     1. Initialize dp array of size n+1.
     2. Loop from 1 to n:
        - If i is even → dp[i] = dp[i/2]
        - If i is odd → dp[i] = dp[i-1] + 1
     3. Return dp array.

     Full Example (n = 8):
     i | binary | even/odd | dp[i] calculation     | dp[i]
    ---|--------|-----------|---------------------|------
     0 | 0      | even      | base case            | 0
     1 | 1      | odd       | dp[0] + 1            | 1
     2 | 10     | even      | dp[1]                | 1
     3 | 11     | odd       | dp[2] + 1            | 2
     4 | 100    | even      | dp[2]                | 1
     5 | 101    | odd       | dp[4] + 1            | 2
     6 | 110    | even      | dp[3]                | 2
     7 | 111    | odd       | dp[6] + 1            | 3
     8 | 1000   | even      | dp[4]                | 1

     Time Complexity: O(n) → single pass
     Space Complexity: O(n) → dp array
    */
    private static int[] countBitsOptimized1(int n){
        int[] dp = new int[n+1];

        for(int i=1; i<=n; i++){
            if((i&1) == 1){ // odd
                dp[i] = dp[i-1] + 1;
            }else{ // even
                dp[i] = dp[i/2];
            }
        }

        return dp;
    }

    /*
     Optimized Approach 2 (Same idea as Optimized1, different implementation):

     Key Idea:
     - We still use previously computed results (dynamic programming).
     - Instead of checking odd/even explicitly:
         1. We find dp[i >> 1], which gives the number of 1s in the "last even number" before i (i/2).
         2. Then we check if the current number i is odd using (i & 1):
             - If i is odd → last bit is 1 → add 1
             - If i is even → last bit is 0 → add 0
     - Formula: dp[i] = dp[i >> 1] + (i & 1)


     Time Complexity: O(n)
     Space Complexity: O(n)
    */
    private static int[] countBitsOptimized2(int n){
        int[] dp = new int[n+1];

        for(int i=1; i<=n; i++){
            dp[i] = dp[i >> 1] + (i & 1);
        }

        return dp;
    }
}
