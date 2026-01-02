// https://leetcode.com/problems/multiply-strings/description/

import java.math.BigInteger;

public class MultiplyStrings {
    public static void main(String[] args) {

        /*
        Problem Statement:
        Implement multiply(num1, num2) which multiplies two non-negative integers
        represented as strings and returns the result as a string.

        Constraints:
        - num1 and num2 are strings containing only digits '0'-'9'.
        - You cannot convert the entire string to an integer directly (except in brute-force).
        - num1 and num2 can be very large (beyond 32-bit or 64-bit integer limits).

        Example 1:
        Input: num1 = "2", num2 = "3"
        Output: "6"

        Example 2:
        Input: num1 = "123", num2 = "45"
        Output: "5535"
        */

        String num1 = "2";
        String num2 = "3";

        // Brute-force approach using BigInteger → handles very large numbers
        System.out.println(multiplyBrute(num1, num2));

        // Optimized approach using manual digit multiplication → O(n*m)
        System.out.println(multiplyOptimized(num1, num2));
    }

    /*
    Approach 1: Brute Force (BigInteger Multiplication)
    ---------------------------------------------------
    Idea and Thought Process:

    1. Understanding the Problem:
       - Strings can be extremely long, so we cannot use int or long safely.
         For example, int can store up to 2^31 - 1 (~2 billion), long can store up to 2^63 - 1 (~9 quintillion).
         Multiplying very large numbers as integers will cause overflow.
       - BigInteger in Java is a class that can handle arbitrarily large integers, beyond the limits of int or long.

    2. What is BigInteger:
       - BigInteger is part of java.math package.
       - It allows storage and arithmetic on integers of **any size**.
       - Supports operations like add, subtract, multiply, divide, modulo, power, etc.
       - BigInteger stores numbers as arrays of digits internally, so operations are slower than primitive types.

    3. Algorithm:
       - Convert num1 and num2 to BigInteger objects.
       - Use BigInteger.multiply() method to calculate the product.
       - Convert the result back to string to return.

    4. Complexity Analysis:
       - Time Complexity: O(n * m) internally, where n and m are number of digits in num1 and num2.
       - Space Complexity: O(n + m), depends on number of digits in the result.

    5. Drawback:
       - Relies on built-in BigInteger class.
       - Not allowed in some coding interviews that expect manual implementation.
    */
    private static String multiplyBrute(String num1, String num2){
        BigInteger a = new BigInteger(num1);
        BigInteger b = new BigInteger(num2);
        BigInteger product = a.multiply(b);

        return product.toString();
    }

    /*
    Approach 2: Optimized (Manual Digit Multiplication)
    ---------------------------------------------------
    Idea and Thought Process:

    1. Key Insight:
       - Multiply numbers like you do by hand.
       - Each digit of num1 multiplies each digit of num2.
       - Store intermediate sums in an array, then handle carry.

    2. Handling Zeros:
       - If either num1 or num2 is "0", return "0" immediately.

    3. Algorithm:
       - Create an array 'res' of length num1.length() + num2.length()
         (maximum possible digits in result).
       - Loop over num1 and num2 from right to left:
         a) Multiply the digits.
         b) Add to current position in res array.
         c) Handle carry: last digit goes in current position, carry added to left.
       - Convert res array to string, skipping leading zeros.

    4. Complexity Analysis:
       - Time Complexity: O(n * m), where n = num1.length(), m = num2.length().
       - Space Complexity: O(n + m), array to hold result digits.

    5. Full Dry Run Example:
       Input: num1 = "123", num2 = "45"

    Step 0: Initialize
    -----------------
    num1Length = 3, num2Length = 2
    res = [0, 0, 0, 0, 0]  // size = 3 + 2

    Step 1: Multiply last digit of num1 (3) with num2 digits
    --------------------------------------------------------
    i = 2 (num1[2] = '3')

    j = 1 (num2[1] = '5')
    - multiplication = 3 * 5 = 15
    - sum = multiplication + res[i+j+1] = 15 + res[4] = 15 + 0 = 15
    - res[4] = sum % 10 = 15 % 10 = 5
    - res[3] += sum / 10 = 0 + 15/10 = 1
    res = [0, 0, 0, 1, 5]

    j = 0 (num2[0] = '4')
    - multiplication = 3 * 4 = 12
    - sum = 12 + res[3] = 12 + 1 = 13
    - res[3] = 13 % 10 = 3
    - res[2] += 13 / 10 = 0 + 1 = 1
    res = [0, 0, 1, 3, 5]

    Step 2: Multiply second last digit of num1 (2) with num2 digits
    ----------------------------------------------------------------
    i = 1 (num1[1] = '2')

    j = 1 (num2[1] = '5')
    - multiplication = 2 * 5 = 10
    - sum = 10 + res[3] = 10 + 3 = 13
    - res[3] = 13 % 10 = 3
    - res[2] += 13 / 10 = 1 + 1 = 2
    res = [0, 0, 2, 3, 5]

    j = 0 (num2[0] = '4')
    - multiplication = 2 * 4 = 8
    - sum = 8 + res[2] = 8 + 2 = 10
    - res[2] = 10 % 10 = 0
    - res[1] += 10 / 10 = 0 + 1 = 1
    res = [0, 1, 0, 3, 5]

    Step 3: Multiply first digit of num1 (1) with num2 digits
    ----------------------------------------------------------
    i = 0 (num1[0] = '1')

    j = 1 (num2[1] = '5')
    - multiplication = 1 * 5 = 5
    - sum = 5 + res[1+0+1] = 5 + res[2] = 5 + 0 = 5
    - res[2] = 5 % 10 = 5
    - res[1] += 5 / 10 = 1 + 0 = 1
    res = [0, 1, 5, 3, 5]

    j = 0 (num2[0] = '4')
    - multiplication = 1 * 4 = 4
    - sum = 4 + res[0+0+1] = 4 + res[1] = 4 + 1 = 5
    - res[1] = 5 % 10 = 5
    - res[0] += 5 / 10 = 0 + 0 = 0
    res = [0, 5, 5, 3, 5]

    Step 4: Convert res array to string
    -----------------------------------
    res = [0, 5, 5, 3, 5]
    - Skip leading zero (res[0] = 0)
    - Append 5 → "5"
    - Append 5 → "55"
    - Append 3 → "553"
    - Append 5 → "5535"

    Final Result = "5535"
    */
    private static String multiplyOptimized(String num1, String num2){
        if(num1.equals("0") || num2.equals("0"))
            return "0";

        int num1Length = num1.length();
        int num2Length = num2.length();

        int[] res = new int[num1Length + num2Length];

        for(int i=num1Length-1; i>=0; i--){
            for(int j=num2Length-1; j>=0; j--){
                int multiplication = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int sum = multiplication + res[i + j + 1];

                res[i + j + 1] = sum % 10;    // Store current digit
                res[i + j] += sum / 10;        // Carry goes to the left
            }
        }

        StringBuilder finalResult = new StringBuilder();
        for(int num : res){
            boolean isLeadingZero = finalResult.length() == 0 && num == 0;
            if(!isLeadingZero)
                finalResult.append(num);
        }

        return finalResult.toString();

    }
}
