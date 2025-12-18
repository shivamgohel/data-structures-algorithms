// https://leetcode.com/problems/reverse-bits/

public class ReverseBits {
    public static void main(String[] args) {

        /*
         Problem Statement:
         You are given a 32-bit unsigned integer n.
         Your task is to reverse the bits of n and return the resulting integer.

         Example:
         Input:  n = 43261596   (binary: 00000010100101000001111010011100)
         Output: 964176192       (binary: 00111001011110000010100101000000)
        */

        int n = 43261596;

        // Brute-force: convert to binary string, reverse, compute, TC: O(32)
        System.out.println(reverseBitsBrute(n));

        // Better approach: bit manipulation, shift and append LSB, TC: O(32)
        System.out.println(reverseBitsBetter(n));

        // Optimized: reverse by masks in log steps, TC: O(1)
        System.out.println(reverseBitsOptimized(n));
    }

    /*
     Brute-force Approach:
     - Convert the number n to a 32-bit binary string.
     - Reverse the string.
     - Convert the reversed string back to an integer.

     Steps:
     1. Loop through each bit of n (0 to 31):
        - Use (n & (1 << i)) to check if the ith bit is 1.
        - Append '1' or '0' to a StringBuilder.
     2. Reverse the StringBuilder.
     3. Loop through the reversed string:
        - If character is '1', set corresponding bit in result using res |= (1 << i).

     Time Complexity: O(32) → constant, Space Complexity: O(32)
    */
    private static int reverseBitsBrute(int n){
        StringBuilder binary = new StringBuilder();
        for(int i=0; i<32; i++){
            if((n & (1 << i)) != 0){
                binary.append("1");
            }else{
                binary.append("0");
            }
        }

        String reversedString = binary.reverse().toString();
        int res = 0;
        for(int i=0; i<32; i++){
            if(reversedString.charAt(i) == '1'){
                res |= (1 << i);
            }
        }

        return res;
    }

    /*
     Better Approach (Bit Manipulation):
     - Instead of converting the number to a string, we can reverse the bits directly using shifts.
     - The idea is to take the least significant bit (LSB) of n one by one and append it to the result,
       while shifting the result to make space for the next bit.

     Steps:
     1. Initialize result = 0.
     2. Loop 32 times (since we are dealing with a 32-bit integer):
        - Shift result left by 1 (res <<= 1) to make space for the next bit.
        - Extract the LSB of n using (n & 1) and append it to result using (res |= n & 1).
          * LSB (Least Significant Bit) is the rightmost bit in the binary representation.
          * n & 1 gives 1 if the last bit of n is 1, otherwise 0.
        - Shift n right by 1 (n >>= 1) to move to the next bit.
     3. After the loop, result contains the reversed bits of n.
     4. Return result.

     Why this works:
     - By always shifting the result left, we "push" the bits to their reversed positions.
     - By extracting LSB of n and appending it, we ensure the first bit of n becomes the last bit of result,
       the second bit of n becomes the second last, and so on.

     Time Complexity: O(32) → constant
     Space Complexity: O(1)
    */
    private static int reverseBitsBetter(int n){
        int res = 0;

        for(int i=0; i<32; i++){
            res = res << 1;
            res = res | (n & 1);
            n = n >> 1;
        }

        return res;
    }

    /*
     Optimized Approach (Divide-and-Conquer with Bit Masking):
     - This method reverses bits by swapping groups of bits in a structured way,
       instead of iterating 32 times.
     - The idea is to swap:
         1. Half-words (16-bit blocks)
         2. Bytes (8-bit blocks)
         3. Nibbles (4-bit blocks)
         4. Pairs of bits (2-bit blocks)
         5. Individual bits (1-bit)
     - This is done using masks and shift operations.

     Steps:
     1. Swap 16-bit halves:
        - ret = (ret >>> 16) | (ret << 16)
          * ">>>" is unsigned right shift to avoid sign extension.
     2. Swap bytes within each 16-bit half:
        - ret = (ret & 0xff00ff00) >>> 8 | (ret & 0x00ff00ff) << 8
          * Masking separates even and odd bytes, then shift and merge.
     3. Swap nibbles (4-bit groups):
        - ret = (ret & 0xf0f0f0f0) >>> 4 | (ret & 0x0f0f0f0f) << 4
     4. Swap pairs of bits:
        - ret = (ret & 0xcccccccc) >>> 2 | (ret & 0x33333333) << 2
          * 0b11001100... mask isolates pairs of bits.
     5. Swap individual bits:
        - ret = (ret & 0xaaaaaaaa) >>> 1 | (ret & 0x55555555) << 1
          * 0b10101010... mask isolates alternating bits.

     Why this works:
     - We are progressively swapping larger to smaller groups of bits.
     - Masks allow us to select and move only certain bits at a time.
     - This avoids a loop and directly computes the reversed integer efficiently.

     Time Complexity: O(1) → constant, only a fixed number of operations
     Space Complexity: O(1)
    */
    private static int reverseBitsOptimized(int n){
        int ret = n;
        ret = ret >>> 16 | ret << 16;
        ret = (ret & 0xff00ff00) >>> 8 | (ret & 0x00ff00ff) << 8;
        ret = (ret & 0xf0f0f0f0) >>> 4 | (ret & 0x0f0f0f0f) << 4;
        ret = (ret & 0xcccccccc) >>> 2 | (ret & 0x33333333) << 2;
        ret = (ret & 0xaaaaaaaa) >>> 1 | (ret & 0x55555555) << 1;
        return ret;
    }
}
