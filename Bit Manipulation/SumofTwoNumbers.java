// https://leetcode.com/problems/sum-of-two-integers/description/

public class SumofTwoNumbers {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given two integers a and b, return the sum of the two integers
         without using the '+' or '-' operators.

         Example:
         Input: a = 2, b = 3
         Output: 5
        */

        int a = 2;
        int b = 3;

        // Brute-force approach using normal addition
        System.out.println(getSumBrute(a,b));

        // Optimized approach using bit manipulation (XOR and carry)
        System.out.println(getSumOptimzied(a, b));
    }

    /*
     Brute-force Approach:
     - Simply use the built-in '+' operator to add the two numbers.
     - This works but does not satisfy the "no + or -" constraint of the problem.

     Time Complexity: O(1)
     Space Complexity: O(1)
    */
    private static int getSumBrute(int a, int b){
        return a + b;
    }

    /*
     Optimized Approach (Bit Manipulation):
     - Use XOR (^) and AND (&) operators to calculate sum without '+'.

     Steps:
     1. XOR operation (a ^ b):
        - Gives the sum of bits **without carry**.
        - Example: 1 ^ 1 = 0 (carry ignored), 1 ^ 0 = 1

     2. AND operation (a & b):
        - Identifies **carry bits** where both bits are 1.
        - Shift carry left by 1 (carry << 1) to align it for next addition.

     3. Loop until carry becomes 0:
        - At each iteration:
            a = a ^ b     → sum without carry
            b = carry     → next carry
        - Repeat until b (carry) is 0.

     4. Return a:
        - When b == 0, there is no carry left.
        - a now contains the full sum.

     Example (a = 2, b = 3):
     - Binary:
         2 =  10
         3 =  11

     Iteration 1:
         carry = (10 & 11) << 1 = 10 << 1 = 100 (4)
         a = 10 ^ 11 = 01 (1)
         b = carry = 100 (4)

     Iteration 2:
         carry = (01 & 100) << 1 = 0
         a = 01 ^ 100 = 101 (5)
         b = 0 → loop ends

     Result: a = 101 (binary) = 5 (decimal)

     Time Complexity: O(1) → because integers are 32-bit, loop runs at most 32 times
     Space Complexity: O(1)
    */
    private static int getSumOptimzied(int a, int b){
        while(b != 0){
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }

        return a;
    }

}
