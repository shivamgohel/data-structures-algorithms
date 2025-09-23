// https://leetcode.com/problems/valid-palindrome/description/
// import java.util.*;

class ValidPalindrome
{
    public static void main(String[] args) 
    {
        /*
        Problem Statement:
        Given a string s, determine if it is a palindrome, considering only alphanumeric characters 
        and ignoring cases.
        */
        
        String s = "tac a cat";

        // Brute-force approach: O(n) time complexity, O(n) space complexity
        System.out.println(validPalindromeBrute(s));
        
        // Optimized two-pointer approach: O(n) time complexity, O(1) space complexity
        System.out.println(validPalindromeOptimized(s));

    }

    /*
     * Brute-force approach: 
     * - Removes non-alphanumeric characters and converts the string to lowercase.
     * - Reverses the cleaned string and checks if it matches the original cleaned version.
     * 
     * Time Complexity: O(n) - Iterates through the string twice (once for cleaning, once for reversal).
     * Space Complexity: O(n) - Uses extra space for the cleaned string.
    */
    private static boolean validPalindromeBrute(String s){
        StringBuilder cleaned_string = new StringBuilder();

        for(char c : s.toCharArray())
        {
            if(Character.isLetterOrDigit(c))
                cleaned_string.append(Character.toLowerCase(c));
        }

        String original_string = cleaned_string.toString();
        String reverse_string = cleaned_string.reverse().toString();

        return original_string.equals(reverse_string);
    }

    /*
     * Optimized Two-pointer approach:
     * - Uses two pointers (left and right) to compare characters directly.
     * - Skips non-alphanumeric characters and converts to lowercase during comparison.
     * 
     * Time Complexity: O(n) - Single pass through the string.
     * Space Complexity: O(1) - Uses constant extra space.
    */
    private static boolean validPalindromeOptimized(String s) {
        int left = 0;
        int right = s.length()-1;

        while(left < right)
        {   
            // Skip non-alphanumeric characters
            while(left < right && !Character.isLetterOrDigit(s.charAt(left)))
                left++;
            while(left < right && !Character.isLetterOrDigit(s.charAt(right)))
                right--;

            // Compare characters after converting to lowercase
            if(Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right)))
                return false;

            left++;
            right--;            
        }

        return true;
    }

/*
 * Optimized2: Clean-then-compare approach but space complexity is O(n).
 * 
 * - First, removes all non-alphanumeric characters using regex.
 * - Converts the cleaned string to lowercase.
 * - Then, uses the two-pointer technique to check if it's a palindrome.
 *
 * This approach is a hybrid: it's more readable and concise than manually
 * checking character types, while still being fairly efficient.
 * 
 * Time Complexity: O(n)
 * - replaceAll() and toLowerCase() each scan the string once.
 * - The two-pointer check is another pass.
 * - So overall still linear.
 * 
 * Space Complexity: O(n)
 * - Because a new cleaned string is created before comparison.
*/
    private static boolean validPalindromeOptimized2(String s) {
        s = s.replaceAll("[^a-zA-Z0-9]", "");
        s = s.toLowerCase();
        
        int left = 0;
        int right = s.length() - 1;

        while(left < right){
            if(s.charAt(left) != s.charAt(right))
                return false;
            
            left++;
            right--;
        }

        return true;
    }
}

