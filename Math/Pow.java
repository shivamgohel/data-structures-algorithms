// https://leetcode.com/problems/powx-n/description/

public class Pow {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Implement pow(x, n), which calculates x raised to the power n (x^n).

         Constraints:
         - x is a double.
         - n is a 32-bit signed integer.
         - n can be negative.
         - Must handle large values of n efficiently.

         Example 1:
         Input: x = 2.0, n = 10
         Output: 1024.0

         Example 2:
         Input: x = 2.0, n = -2
         Output: 0.25
        */

        double x = 2.0;
        int n = 10;

        // Brute-force approach → TC: O(n), SC: O(1)
        System.out.println(myPowBrute(x, n));

        // Optimized approach (Binary Exponentiation) → TC: O(log n), SC: O(1)
        System.out.println(myPowOptimized(x, n));
    }

    /*
     Approach 1: Brute Force (Repeated Multiplication)
     -------------------------------------------------
     Idea and Thought Process:

     1. Understanding the Problem:
        - We need to compute x^n.
        - If n is positive, multiply x by itself n times.
        - If n is negative, compute x^|n| and take reciprocal.

     2. Algorithm:
        - Handle base cases:
            a) If x == 0, result is 0.
            b) If n == 0, result is 1.
        - Convert n to long to safely handle Integer.MIN_VALUE.
        - Take absolute value of n.
        - Multiply x repeatedly |n| times.
        - If original n was negative, return 1 / result.

     3. Why long is used:
        - Integer.MIN_VALUE = -2147483648
        - Math.abs(Integer.MIN_VALUE) overflows.
        - Casting to long avoids overflow.

     4. Complexity Analysis:
        - Time Complexity: O(n)
        - Space Complexity: O(1)

     5. Drawback:
        - Inefficient for large n.
        - Causes Time Limit Exceeded (TLE) on LeetCode.
    */
    private static double myPowBrute(double x, int n){
        if(x == 0) return 0;
        if(n == 0) return 1;

        long power = Math.abs((long) n);
        double res = 1.0;

        for(int i=0; i<power; i++){
            res = res * x;
        }

        return n > 0 ? res : 1/res;
    }


    /*
     Approach 2: Optimized (Binary Exponentiation)
     ---------------------------------------------
     Idea and Thought Process:

     1. Key Insight:
        - Instead of multiplying x n times, we can reduce the problem size
          by half at each step.
        - Uses the idea:
            x^n =
                (x^2)^(n/2)   if n is even
                x * x^(n-1)   if n is odd

     2. Handling Negative Powers:
        - Mathematical rule:
            x^(-n) = 1 / (x^n)
        - Convert negative power into positive by:
            a) x = 1 / x
            b) n = -n

     3. Algorithm:
        - Convert n to long to avoid overflow.
        - If n is negative, flip the base and exponent.
        - Initialize result = 1.
        - While power > 0:
            a) If power is odd:
               - Multiply result by x
               - Decrease power by 1
            b) If power is even:
               - Square x
               - Divide power by 2

     4. Why this works:
        - Each iteration reduces power significantly.
        - Number of multiplications is minimized.

     5. Complexity Analysis:
        - Time Complexity: O(log n)
        - Space Complexity: O(1)

     DRY RUN EXAMPLE:
     Input: x = 2, n = 10

     Step 1: Initialization
     ----------------------
     x = 2
     n = 10
     power = 10
     res = 1

     Step 2: Since power is positive, no base flip needed.

     Step 3: Start while loop (power > 0)

     Iteration 1:
     ------------
     power = 10 (even)
     → Square x and halve power

     x = 2 * 2 = 4
     power = 10 / 2 = 5
     res = 1

     Iteration 2:
     ------------
     power = 5 (odd)
     → Multiply res with x and reduce power by 1

     res = 1 * 4 = 4
     power = 5 - 1 = 4
     x = 4

     Iteration 3:
     ------------
     power = 4 (even)
     → Square x and halve power

     x = 4 * 4 = 16
     power = 4 / 2 = 2
     res = 4

     Iteration 4:
     ------------
     power = 2 (even)
     → Square x and halve power

     x = 16 * 16 = 256
     power = 2 / 2 = 1
     res = 4

     Iteration 5:
     ------------
     power = 1 (odd)
     → Multiply res with x and reduce power by 1

     res = 4 * 256 = 1024
     power = 1 - 1 = 0

     Step 4: Loop ends (power == 0)

     Final Answer:
     -------------
     res = 1024
    */
    private static double myPowOptimized(double x, int n){
        if(x == 0) return 0;
        if(n == 0) return 1;

        long power = n;
        double res = 1.0;

        if(power < 0){
            x = 1 / x;
            power = - power;
        }

        while(power > 0){
            if(power % 2 == 1){
                res = res * x;
                power = power - 1;
            }else{
                x = x * x;
                power = power / 2;
            }
        }

        return res;
    }


}
