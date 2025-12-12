// https://leetcode.com/problems/number-of-1-bits/description/

public class Numberof1Bits {

    static void main() {

        /*
         Problem Statement:
         You are given a non-negative integer `n` representing a 32-bit unsigned value.
         Return the number of '1' bits in its binary representation (also known as the Hamming Weight).

         Constraints:
         - n fits within a 32-bit unsigned integer.
         - You may assume n >= 0.
         - Time complexity requirement: O(1) per bit operation.
         - Try multiple approaches.
        */

        // Brute-force approach: Check bit-by-bit using right shift (O(32))
        hammingWeightBrute(11);

        // Better approach: Loop until number becomes 0 (O(# of bits until zero))
        hammingWeightBetter(11);

        // Optimized approach: Brian Kernighan’s Algorithm (O(# of 1-bits))
        hammingWeightOptimized(11);

        // In-built approach: Using Integer.bitCount()
        hammingWeightInBuilt(11);


    }

    /*
   Brute-force Approach:
   - Since n is a 32-bit number, check each bit individually.
   - Use (n & 1) to check if the least significant bit is 1.
   - Then right-shift n one bit at a time for 32 iterations.

   Steps:
   1. Initialize a counter to store the number of 1-bits.
   2. Loop exactly 32 times.
   3. Check LSB using (n & 1).
   4. Increment count if LSB is 1.
   5. Right shift the number.

   Time Complexity: O(32) → effectively O(1)
   Space Complexity: O(1)
   */
    private static int hammingWeightBrute(int n) {
        int count = 0;
        for(int i=0; i<32; i++){
            if((1 & n) == 1)
                count++;

            n >>= 1;
        }

        return count;
    }

    /*
   Better Approach:
   - Instead of looping fixed 32 iterations, loop while n != 0.
   - Still check the least significant bit using (n & 1).

   Steps:
   1. Loop until n becomes zero.
   2. Check the least significant bit.
   3. Increment count when the bit is 1.
   4. Right shift n.

   Time Complexity: O(# of shifts until n becomes 0)
   Space Complexity: O(1)
   */
    private static int hammingWeightBetter(int n){
        int count = 0;
        while(n != 0){
            if((1 & n) == 1)
                count++;

            n >>= 1;
        }

        return count;
    }

    /*
   Optimized Approach (Brian Kernighan’s Algorithm):
   Key Insight:
   - n & (n - 1) removes the lowest set bit (rightmost 1-bit).
   - This directly counts only the 1-bits, making it very efficient.

   Steps:
   1. While n != 0:
      - Update n = n & (n - 1) to drop one 1-bit.
      - Increment count.
   2. Stop when all 1-bits are removed.

   Why It Works:
   - Each iteration removes exactly one '1' bit.
   - Runs in O(number_of_1_bits) → faster for sparse binary numbers.

   Time Complexity: O(# of 1-bits)
   Space Complexity: O(1)
   */
    private static int hammingWeightOptimized(int n){
        int count = 0;
        while(n != 0){
            n &= n-1;
            count++;
        }

        return count;
    }

    /*
   In-Built Approach:
   - Java provides Integer.bitCount(n) which returns the number of 1-bits.
   - Uses optimized CPU-level instructions internally.

   Steps:
   1. Return Integer.bitCount(n).

   Time Complexity: O(1) (intrinsic)
   Space Complexity: O(1)
   */
    private static int hammingWeightInBuilt(int n){
        return Integer.bitCount(n);
    }


}
