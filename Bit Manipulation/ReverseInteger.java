// https://leetcode.com/problems/reverse-integer/description/

public class ReverseInteger {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given a 32-bit signed integer x, reverse its digits.
         If reversing x causes the value to go outside the
         signed 32-bit integer range [-2^31, 2^31 - 1],
         return 0.

         Example:
         Input:  x = 123
         Output: 321

         Input:  x = -123
         Output: -321

         Input:  x = 1534236469
         Output: 0 (overflow)
        */

        // Brute-force approach using string reversal
        System.out.println(reverseBrute(123));

        // Better approach using long to detect overflow
        System.out.println(reverseBetter(123));

        // Optimized approach using math + overflow check
        System.out.println(reverseOptimized(123));
    }

    /*
     Approach 1: Brute-force using String Reversal
     -------------------------------------------------
     Idea:
     - Convert the absolute value of the integer to a string
     - Reverse the string using StringBuilder
     - Convert the reversed string back to integer
     - Apply negative sign if needed
     - Catch NumberFormatException if reversed value exceeds 32-bit integer bounds

     Time Complexity:  O(n) → n = number of digits
     Space Complexity: O(n) → for string and StringBuilder
    */
    public static int reverseBrute(int x){
        boolean isNegative = x < 0;

        String s = Integer.toString(Math.abs(x));
        String reversed = new StringBuilder(s).reverse().toString();

        try{
            int val = Integer.parseInt(reversed);
            return isNegative ? -val : val;
        }
        catch(NumberFormatException e){
            return 0;
        }
    }

    /*
     Approach 2: Using long to detect overflow
     -------------------------------------------------
     Idea:
     - Reverse the integer mathematically using modulo and division
     - Store result in long to handle overflow temporarily
     - Check bounds before returning

     Steps:
     1. Initialize rev = 0 (long)
     2. Loop while x != 0
        - Extract last digit: x % 10
        - Update rev = rev * 10 + digit
        - Remove last digit from x: x / 10
     3. After loop, check if rev is within int bounds
        - If yes, cast to int and return
        - Else, return 0

     Time Complexity:  O(log10(n)) → number of digits
     Space Complexity: O(1)
    */
    public static int reverseBetter(int x){
        long rev = 0;

        while(x != 0){
            rev = rev * 10 + (x % 10);
            x = x / 10;
        }

        if(rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE){
            return 0;
        }

        return (int) rev;
    }


    /*
     Optimized Approach (Mathematical Digit Reversal):

     - Reverse the integer digit by digit using modulo (%) and division (/).
     - Carefully check for overflow before updating the reversed number.

     Steps:
     1. Extract last digit:
        - digit = x % 10
        - This gives the last digit of the number.

     2. Remove last digit:
        - x = x / 10
        - Shrinks the number for the next iteration.

     3. Overflow check BEFORE updating rev:
        - Since rev = rev * 10 + digit,
          multiplying rev by 10 may cause overflow.

        Conditions:
        - If rev > Integer.MAX_VALUE / 10
        - OR rev == Integer.MAX_VALUE / 10 and digit > 7
          → overflow for positive numbers

        - If rev < Integer.MIN_VALUE / 10
        - OR rev == Integer.MIN_VALUE / 10 and digit < -8
          → overflow for negative numbers

     4. Update reversed number:
        - rev = rev * 10 + digit

     5. Repeat until x becomes 0.

     Example (Normal Case: x = 123):
     ------------------------------
     Initial:
       x = 123, rev = 0

     Iteration 1:
       digit = 3
       rev = 0 * 10 + 3 = 3
       x = 12

     Iteration 2:
       digit = 2
       rev = 3 * 10 + 2 = 32
       x = 1

     Iteration 3:
       digit = 1
       rev = 32 * 10 + 1 = 321
       x = 0 → loop ends

     Result: 321

     Example (Overflow Case: x = 1534236469):
     ---------------------------------------
     Integer.MAX_VALUE = 2147483647

     At some point during reversal:
       rev = 964632435
       digit = 1

     Check:
       rev > Integer.MAX_VALUE / 10
       964632435 > 214748364 → TRUE

     Since multiplying rev by 10 would overflow,
     the function immediately returns 0.

     Result: 0

     Time Complexity:
       O(log10(n)) → number of digits in x

     Space Complexity:
       O(1) → constant extra space
    */
    public static int reverseOptimized(int x){
        int rev = 0;

        while(x != 0){
            int digit = x % 10;
            x = x / 10;


            /*
             Integer Limits (32-bit signed):
             - Maximum value:  2^31 - 1 =  2147483647
             - Minimum value: -2^31     = -2147483648
             */
            if(rev > Integer.MAX_VALUE/10 || (rev == Integer.MAX_VALUE/10 && digit > 7)){
                return 0;
            }
            if(rev < Integer.MIN_VALUE/10 || (rev == Integer.MIN_VALUE/10 && digit < -8)){
                return 0;
            }

            rev = rev * 10 + digit;
        }

        return rev;
    }
}
