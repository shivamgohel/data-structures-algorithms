// https://leetcode.com/problems/longest-repeating-character-replacement/description/
import java.util.*;

class LongestRepeatingCharacterReplacement
{
    public static void main(String[] args) 
    {
        /*
         Problem Statement:
         Given a string `s` and an integer `k`, you can perform at most `k` replacements in the string,
         where a replacement is replacing a character with any other character.
         Return the length of the longest substring containing all the same letter you can get after performing at most `k` replacements.
        */
        
        String s = "XYYX";
        int k = 2;

        // Brute-force approach: O(n^2) time, O(k) space
        System.out.println(longestRepeatingCharacterReplacementBrute(s,k));

        // Better approach: O(n * 26) time, O(k) space
        System.out.println(longestRepeatingCharacterReplacementBetter(s,k));

        // Optimized approach 1 (Sliding Window using HashMap): O(n) time, O(k) space
        System.out.println(longestRepeatingCharacterReplacementOptimized1(s,k));

        // Optimized approach 2 (Sliding Window using Array for frequency counting): O(n) time, O(1) space
        System.out.println(longestRepeatingCharacterReplacementOptimized2(s,k));


    }

    /*
     Brute-force Approach:
     - Start with each index `i` and explore every possible substring ending at index `j`.
     - Track the frequency of characters in the current window.
     - If the number of characters that need to be replaced (i.e., the length of the substring minus the frequency of the most frequent character) exceeds `k`, stop and move to the next starting index `i`.
     - Keep track of the longest valid substring found.

     Time Complexity: O(n^2) — two nested loops.
     Space Complexity: O(k) — HashMap to store frequencies of characters.
    */
    private static int longestRepeatingCharacterReplacementBrute(String s, int k) {
        int maximum_length = 0;

        for(int i=0;i<s.length();i++)
        {
            HashMap<Character,Integer> seen = new HashMap<>();
            int maximum_frequency = 0;

            for(int j=i;j<s.length();j++)
            {
                char current_character = s.charAt(j);
                seen.put(current_character, seen.getOrDefault(current_character, 0) +1);
                maximum_frequency = Math.max(maximum_frequency, seen.get(current_character));
                
                int window_length = j - i + 1;
                if(window_length - maximum_frequency > k)
                    break;

                maximum_length = Math.max(maximum_length, window_length);    
            }
        }

        return maximum_length;
    }

    /*
     Better Approach: Sliding Window using HashMap
     - Use a sliding window technique with two pointers (`left` and `right`).
     - Track the frequency of characters within the window using a HashMap.
     - If the window is valid (i.e., the number of characters needing replacement does not exceed `k`), expand the window by moving the `right` pointer.
     - If the window becomes invalid (i.e., we need more than `k` replacements), move the `left` pointer to shrink the window.
     - Recalculate the maximum_frequency as well. 
     - The recalculation of `maximum_frequency` involves checking the entire `HashMap` to find the highest frequency, which takes **O(26)** time since we have at most 26 possible characters (for uppercase English letters).
     - Keep updating the maximum length of valid windows found.
      
     Time Complexity: O(n * 26) — We pass through the string once and recalculate the `maximum_frequency` by checking 26 characters at most.
     Space Complexity: O(k) — HashMap stores frequencies of characters in the window.
    */
    private static int longestRepeatingCharacterReplacementBetter(String s, int k){
        int maximum_length = 0;
        int maximum_frequency = 0;
        HashMap<Character,Integer> seen = new HashMap<>();
        int left = 0;

        for(int right=0;right<s.length();right++)
        {
            char current_character = s.charAt(right);
            seen.put(current_character, seen.getOrDefault(current_character, 0) +1);
            maximum_frequency = Math.max(maximum_frequency, seen.get(current_character));

            int window_length = right - left + 1;
            if(window_length - maximum_frequency > k)
            {
                char left_character = s.charAt(left);
                seen.put(left_character, seen.get(left_character) -1); 
                left++;

                maximum_frequency = 0;
                for(int freq : seen.values())
                    maximum_frequency = Math.max(maximum_frequency,freq);
            }

            maximum_length = Math.max(maximum_length, right - left + 1); // Must recalculate after window possibly shrinks
        }

        return maximum_length;
    }

    /*
     Optimized Approach 1: Sliding Window using HashMap
     - Use a sliding window technique with two pointers (`left` and `right`).
     - Track the frequency of characters within the window using a HashMap.
     - If the window is valid (i.e., the number of characters needing replacement does not exceed `k`), expand the window by moving the `right` pointer.
     - If the window becomes invalid (i.e., we need more than `k` replacements), move the `left` pointer to shrink the window.
     - Keep updating the maximum length of valid windows found.
      
      **Why Not Update `maximum_frequency` During Shrinking?**
         - When the window becomes invalid (i.e., `window_length - maximum_frequency > k`), we shrink the window from the left by moving the `left` pointer.
         - At this point, it's not necessary to update `maximum_frequency` immediately because the goal is to remove characters from the window and make it valid again. As the window shrinks, `maximum_frequency` will automatically reflect the new window's valid maximum frequency.
         - Recalculating `maximum_frequency` is not required after each shrink, as we are ensuring the new window will always have valid frequencies after the left pointer moves, and we will already check whether it's valid by the condition `(window_length - maximum_frequency > k)`.
      
     Time Complexity: O(n) — we only pass through the string once.
     Space Complexity: O(k) — HashMap stores frequencies of characters in the window.
    */
    private static int longestRepeatingCharacterReplacementOptimized1(String s, int k) {
        int maximum_length = 0;
        int maximum_frequency = 0;
        HashMap<Character,Integer> seen = new HashMap<>();
        int left = 0;

        for(int right=0;right<s.length();right++)
        {
            char current_character = s.charAt(right);
            seen.put(current_character, seen.getOrDefault(current_character, 0) +1);
            maximum_frequency = Math.max(maximum_frequency, seen.get(current_character));

            int window_length = right - left + 1;
            if(window_length - maximum_frequency > k)
            {
                char left_character = s.charAt(left);
                seen.put(left_character, seen.get(left_character) -1); 
                left++;
            }

            maximum_length = Math.max(maximum_length, right - left + 1);
        }

        return maximum_length;
    }

    /*
     Optimized Approach 2: Sliding Window using Array for frequency counting
     - Use an array to track the frequency of each character in the window, which allows O(1) access time.
     - The `left` pointer is moved to ensure that the window has at most `k` replacements.
     - This method is more space-efficient compared to using a HashMap since the array has fixed size (26 for uppercase English letters).

     Time Complexity: O(n) — we only pass through the string once.
     Space Complexity: O(1) — fixed size array for storing frequencies of 26 characters.
    */
    private static int longestRepeatingCharacterReplacementOptimized2(String s, int k) {
        int maximum_length = 0;
        int maximum_frequency = 0;
        int left = 0;
        int[] freq = new int[26];

        for(int right=0;right<s.length();right++)
        {
            char current_character = s.charAt(right);
            freq[current_character - 'A']++;
            maximum_frequency = Math.max(maximum_frequency, freq[current_character - 'A']);

            int window_length = right - left + 1;
            if(window_length - maximum_frequency > k)
            {
                char left_character = s.charAt(left);
                freq[left_character - 'A']--;
                left++;
            }

            maximum_length = Math.max(maximum_length, right - left + 1);
        }

        return maximum_length;
    }

}