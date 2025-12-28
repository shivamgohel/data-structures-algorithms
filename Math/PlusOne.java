// https://leetcode.com/problems/plus-one/description/

public class PlusOne {
    public static void main(String[] args) {

        /*
         Problem Statement:
         You are given a large integer represented as an integer array digits,
         where each digits[i] is the ith digit of the integer.
         The digits are ordered from most significant to least significant.

         Increment the large integer by one and return the resulting array of digits.

         Constraints:
         - The integer does not contain any leading zero.
         - Each element in digits is between 0 and 9.

         Example 1:
         Input: digits = [1,2,3]
         Output: [1,2,4]

         Example 2:
         Input: digits = [9,9,9]
         Output: [1,0,0,0]
        */

        int[] digits = {1,2,3};

        // (will give overflow for big numbers) Brute-force approach (Convert to number) → TC: O(n), SC: O(n)
        plusOneBrute(digits);

        // Optimized approach (Carry-based simulation) → TC: O(n), SC: O(1)
        plusOneOptimized1(digits);

        // Optimized approach (Early exit, most efficient) → TC: O(n), SC: O(1)
        plusOneOptimized2(digits);
    }

    /*
     Approach 1: Brute-force (Convert digits to number)
     --------------------------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - The digits array represents a number.
        - We need to add 1 and return the updated digits array.

     2. Algorithm:
        - Convert the digits array into a number.
        - Add 1 to the number.
        - Convert the updated number back into a digits array.

     3. Steps:
        - Traverse digits and build the number.
        - Increment the number by 1.
        - Convert the number to a string.
        - Convert each character back to an integer array.

     4. Complexity Analysis:
        - Time Complexity: O(n)
        - Space Complexity: O(n)

     5. Drawback:
        - This approach is unsafe for very large inputs due to integer overflow.
        - Not recommended for LeetCode constraints.
    */
    private static int[] plusOneBrute(int[] digits){
        long number = 0;
        for(int digit : digits){
            number = (number * 10) + digit;
        }
        number = number + 1;

        String numberString = String.valueOf(number);
        int[] res = new int[numberString.length()];

        for(int i=0; i<numberString.length(); i++){
            res[i] = numberString.charAt(i) - '0';
        }

        return res;
    }

    /*
     Approach 2: Optimized Using Carry Simulation
     -------------------------------------------
     Idea and Thought Process:

     1. Key Insight:
        - Adding 1 to a number can create a carry.
        - Just like manual addition, the carry propagates from
          right to left.
        - Each array element can store only one digit (0–9),
          so overflow must be handled explicitly.

     2. Why Carry Starts as 1:
        - We are adding exactly one to the number.
        - Instead of handling +1 separately, we treat it as an
          initial carry.

     3. Algorithm:
        - Initialize carry = 1.
        - Traverse the digits array from right to left.
        - For each digit:
            a) Compute sum = digit + carry
            b) Update the current digit using:
               - digit = sum % 10
                 → Keeps only the last digit (0–9) at the current position.
            c) Update the carry using:
               - carry = sum / 10
                 → Extracts the overflow (1 if sum ≥ 10, else 0).

     4. Why % and / Are Used:
        - `% 10` gives the digit that remains in the current place.
          Example: 18 % 10 = 8
        - `/ 10` gives the carry that moves to the next place.
          Example: 18 / 10 = 1
        - Together, they perfectly simulate manual addition.

     5. Edge Case:
        - If carry is still 1 after processing all digits
          (e.g., [9,9,9] → 1000),
          the number grows in size.
        - Create a new array with one extra digit and place 1
          at the most significant position.

     6. Complexity Analysis:
        - Time Complexity: O(n), where n is the number of digits.
        - Space Complexity: O(1), excluding the output array.
    */
    private static int[] plusOneOptimized1(int[] digits){
        int carry = 1;
        for(int i=digits.length-1; i>=0; i--){
            int sum = digits[i] + carry;
            digits[i] = sum % 10;
            carry = sum / 10;
        }

        if(carry == 0){
            return digits;
        }

        int[] res = new int[digits.length+1];
        res[0] = 1;

        return res;
    }

    /*
     Approach 3: Optimized with Early Exit (Best Approach)
     -----------------------------------------------------
     Idea and Thought Process:

     1. Key Observation:
        - If a digit is less than 9, we can increment it and stop.
        - Only digits equal to 9 cause a carry.

     2. Algorithm:
        - Traverse digits from right to left.
        - If digits[i] < 9:
            - Increment digits[i].
            - Return the array immediately.
        - If digits[i] == 9:
            - Set digits[i] to 0 and continue.

     3. Final Edge Case:
        - If all digits are 9, the result is [1, 0, 0, ..., 0].

     4. Complexity Analysis:
        - Time Complexity: O(n)
        - Space Complexity: O(1)
    */
    private static int[] plusOneOptimized2(int[] digits){
        for(int i=digits.length-1; i>=0; i--){
            if(digits[i] < 9){ // 0 1 2 .... 8
                digits[i]++;
                return digits;
            }

            // this will execute for digits[i] = 9
            digits[i] = 0;    // 9 + 1 = 10 so 0
        }

        int[] res = new int[digits.length+1];
        res[0] = 1;

        return res;
    }
}
