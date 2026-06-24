// https://leetcode.com/problems/valid-anagram/description/
import java.util.*;

class ValidAnagram
{
    public static void main(String[] args) 
    {   

        /*
          ================================================================================
          PROBLEM STATEMENT:
          Given two strings `s` and `t`, return true if `t` is an anagram of `s`,
          and false otherwise.

          An Anagram is a word or phrase formed by rearranging the letters of a
          different word or phrase, typically using all the original letters exactly once.

          Constraints:
          - 1 <= s.length, t.length <= 5 * 10^4
          - `s` and `t` consist of lowercase English letters.
          ================================================================================
         */

        String s = "abcde";
        String t = "edcba";

        // Approach 1: Brute Force (Scan & Destroy Strings) | TC: O(N^2), SC: O(N)
        System.out.println(validAnagramBrute1(s,t));

        // Approach 2: Better (Standard char[] Sort) | TC: O(N log N), SC: O(N)
        System.out.println(validAnagramBrute2(s,t));

        // Approach 3: Better (Object Collections List Sort) | TC: O(N log N), SC: O(N)
        System.out.println(validAnagramBrute3(s,t));

        // Approach 4: HashMap Dual Frequency Tracker | TC: O(N), SC: O(K)
        System.out.println(validAnagramOptimized1(s, t));

        // Approach 5: Fixed Frequency Array (Two-Pass Bucket Counter) | TC: O(N), SC: O(1)
        System.out.println(validAnagramOptimized2(s, t));

        // Approach 6: HashMap Single Net Frequency Tracker | TC: O(N), SC: O(K)
        System.out.println(validAnagramOptimized3(s, t));

    }

    /*
      Approach 1: Brute Force (Scan & Destroy / Substring Extraction)
      -------------------------------------------------------
      Intuition:
      - For every character in `s`, look up its position in `t` using a linear sweep.
      - Erase the matching character by slicing `t` into substrings to prevent double-counting.

      Key Idea:
      "For every character in the first string, do a linear scan to find and eliminate
       its twin in the second string."

      Step-by-Step:
      1. Sizing Gate: Return false immediately if the strings have different lengths.
      2. Outer Loop: Iterate character-by-character through string `s`.
      3. Lookup: Use `t.indexOf(ch)` to search for the current character's position within `t`.
      4. Match Evaluation: If `indexOf` returns -1, a character in `s` does not exist in `t`; return false.
      5. Destroy/Mutate: If found, remove that character from `t` by slicing the string around it.

      Why it's an anti-pattern for interviews:
      - Extreme scaling bottlenecks. String manipulation inside a loop forces Java to constantly
        allocate new string blocks on the heap, triggering a Time Limit Exceeded (TLE) error.

      Complexity Analysis:
      - Time: O(N^2) -> Outer loop runs N times, inner `indexOf` and `substring` take O(N).
      - Space: O(N) -> Reallocates fresh copies of string fragments on every modification.
    */
    private static boolean validAnagramBrute1(String s, String t){
        if(s.length() != t.length())
            return false;

        for(int i=0; i<s.length(); i++){
            char ch = s.charAt(i);
            int index = t.indexOf(ch);

            if(index == -1)
                return false;

            t = t.substring(0, index) + t.substring(index+1);
        }

        return true;
    }

    /*
      Approach 2: Better (Standard char[] Sort)
      ------------------------------------------------------
      Intuition:
      - Anagrams share identical character distributions but vary in positional layout.
      - Sorting both strings alphabetically strips away structural arrangement variations.

      Key Idea:
      "Re-order both character arrays to transform a structural matching check into a
       direct, sequential index alignment check."

      Step-by-Step:
      1. Quick Check: Return false immediately if string lengths do not match.
      2. Array Conversion: Transmute strings `s` and `t` into mutable primitive arrays (`arrayS`, `arrayT`).
      3. Sort Execution: Sort both character sequences using `Arrays.sort()`.
      4. Sequence Validation: Compare the arrays index-by-index using `Arrays.equals()`.

      Complexity Analysis:
      - Time: O(N log N) -> Driven entirely by Dual-Pivot Quicksort / Timsort overhead.
      - Space: O(N) -> Requires allocating underlying primitive character arrays matching string lengths.
    */
    private static boolean validAnagramBrute2(String s, String t){
        if(s.length() != t.length())
            return false;

        char[] arrayS = s.toCharArray();
        char[] arrayT = t.toCharArray();

        Arrays.sort(arrayS);
        Arrays.sort(arrayT);

        return Arrays.equals(arrayS, arrayT);   
    }

    /*
      Approach 3: Better (Object Collections List Sort)
      ------------------------------------------------------
      Intuition:
      - Instead of a low-level primitive `char[]` array, unpack the components into an
        Object-based collection `ArrayList<Character>`.
      - Leverages Java's `Collections.sort()` (Timsort architecture) to establish alignment.

      Key Idea:
      "Wrap character tokens inside heap objects to process them via core Collections Framework API toolsets."

      Step-by-Step:
      1. Pre-Check: Validate sizing lengths.
      2. Population: Cycle through both sequences to feed character objects into generic lists.
      3. Collections Sort: Run `Collections.sort()` to sort the object structures.
      4. List Equality Check: Run `sList.equals(tList)`. Abstract lists evaluate structure automatically.

      Complexity Analysis:
      - Time: O(N log N) -> Sorting collections of object wrappers operates at log-scale boundaries.
      - Space: O(N) -> Memory scales slightly higher here due to object boxing overhead.
    */
    private static boolean validAnagramBrute3(String s, String t){
        if(s.length() != t.length())
            return false;

        List<Character> sList = new ArrayList<>();
        List<Character> tList = new ArrayList<>();

        for(int i=0; i<s.length(); i++){
            sList.add(s.charAt(i));
            tList.add(t.charAt(i));
        }

        Collections.sort(sList);
        Collections.sort(tList);

        return sList.equals(tList);
    }

    /*
      Approach 4: Optimized (HashMap Dual Frequency Tracker)
      ------------------------------------------------------
      Intuition:
      - Hash-based lookups offer quick structural analysis without sorting overhead.
      - Mapping characters to their direct occurrence frequencies lets us check distribution balances.

      Key Idea:
      "Map out character counts using key-value pair buckets to process structural layouts."

      Step-by-Step:
      1. Guard Check: Confirm identical string lengths.
      2. Registration Pass: Iterate across the items, registering frequencies across separate map buffers.
      3. Comparison: Invoke `.equals()` to check if both maps match in terms of content keys and total values.

      Drawback:
      - Instantiating two full object map buffers introduces redundant heap allocation overhead.

      Complexity Analysis:
      - Time: O(N) -> Single pass with average O(1) hashing transactions.
      - Space: O(N) -> Allocates memory space corresponding to unique tracking entries.
    */
    private static boolean validAnagramOptimized1(String s, String t){
        if(s.length() != t.length())
            return false;

        HashMap<Character,Integer> hashS = new HashMap<>();
        HashMap<Character,Integer> hashT = new HashMap<>();

        for(int i=0;i<s.length();i++)
        {
            hashS.put(s.charAt(i), hashS.getOrDefault(s.charAt(i), 0) +1);
            hashT.put(t.charAt(i), hashT.getOrDefault(t.charAt(i), 0) +1);
        }

        return hashS.equals(hashT);    
    }

    /*
      Approach 5: Optimized (Fixed Frequency Array Counter)
      ------------------------------------------------------
      Intuition:
      - Since the constraints specify lowercase English letters, we can map characters to a simple array of size 26.
      - We project characters to indices via ASCII math: `character - 'a'`.
      - We increment counters for string `s` and decrement for string `t`. An anagram must leave every cell at zero.

      Key Idea:
      "Map characters to fixed numerical index buckets; balance counts out by adding
       from the first string and subtracting from the second."

      Step-by-Step:
      1. Pre-Check: Validate sizing lengths.
      2. Bucket Setup: Instantiate a primitive integer array `freqArray` of fixed size 26.
      3. Balance Pass: Simultaneously increment positions for `s` and decrement for `t`.
      4. Verification Pass: Iterate through the buckets. If any entry is not equal to 0, return false.

      Complexity Analysis:
      - Time: O(N) -> Linear pass to process frequencies, followed by a fixed 26-step scan.
      - Space: O(1) -> The array allocation size remains constant at 26 elements regardless of string growth.
    */
    private static boolean validAnagramOptimized2(String s, String t){
        if(s.length() != t.length())
            return false;

        int[] freqArray = new int[26];

        // Increase for 's' characters, decrease for 't' characters
        for(int i=0;i<s.length();i++)
        {
            freqArray[s.charAt(i) - 'a']++;
            freqArray[t.charAt(i) - 'a']--;
        }

        for(int x : freqArray)
        {
            if(x!=0)
                return false;
        }

        return true;    
    }

    /*
      Approach 6: Optimized (HashMap Single Net Frequency Tracker)
      ------------------------------------------------------
      Intuition:
      - Optimizes Approach 4 by using a single Map instance instead of two separate object containers.
      - We increment frequencies for incoming tokens from `s` and decrement counts for incoming tokens from `t`.
      - If characters balance perfectly, every dynamic map value key must end up back at exactly 0.

      Key Idea:
      "Utilize a single dynamic frequency counter, using addition and subtraction to track the net balance of all characters."

      Step-by-Step:
      1. Safety Gate: Confirm structural sizing boundaries line up cleanly.
      2. Population Pass: Loop through both strings simultaneously. Read character values, adding +1 to the map index for values from `s` and subtracting -1 for values from `t`.
      3. Balance Audit: Walk the value collection of the map via `.values()`. If any counter key fails to equal 0, return false.

      Complexity Analysis:
      - Time: O(N) -> Single structural iteration with optimal O(1) key mutation lookups on average.
      - Space: O(K) -> Allocates memory space dynamically matching unique character counts ($K$) instead of fixed limits.
    */
    private static boolean validAnagramOptimized3(String s, String t){
        if(s.length() != t.length())
            return false;

        HashMap<Character, Integer> map = new HashMap<>();

        for(int i=0; i<s.length(); i++){
            char charS = s.charAt(i);
            char charT = t.charAt(i);

            map.put(charS, map.getOrDefault(charS, 0) +1);
            map.put(charT, map.getOrDefault(charT, 0) -1);
        }

        for(int count : map.values()){
            if(count != 0)
                return false;
        }

        return true;
    }

}