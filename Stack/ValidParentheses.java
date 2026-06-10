// https://leetcode.com/problems/valid-parentheses/description/

import java.util.*;

public class ValidParentheses {
    public static void main(String[] args) {

        /*
          ================================================================================
          PROBLEM STATEMENT:
          Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']',
          determine if the input string is valid.

          An input string is valid if:
          1. Open brackets must be closed by the same type of brackets.
          2. Open brackets must be closed in the correct order.
          3. Every close bracket has a corresponding open bracket of the same type.

          Constraints:
          - 1 <= s.length <= 10^4
          - `s` consists of parentheses only '()[]{}'.
          ================================================================================
         */

        String s = "()[]{}";

        // Approach 1: Brute Force | TC: O(N^2), SC: O(N) Due to String Immutability
        System.out.println(isValidParenthesesBrute(s));

        // Approach 2: Optimized One Pass | TC: O(N), SC: O(N) Auxiliary Space
        System.out.println(isValidParenthesesOptimized(s));

    }

     /*
      Approach 1: Brute Force (String Replacement Loop)
      -------------------------------------------------------
      Intuition:
      - A valid parentheses string must contain at least one nested or adjacent matching
        primitive pair (like "()", "[]", or "{}") somewhere inside it.
      - If we continuously scan the string and strip out these basic pairs, the outer
        brackets will collapse inward, exposing new adjacent matching pairs.
      - If the string successfully collapses into an empty string, it is valid.

      Key Idea:
      "Repeatedly eliminate adjacent matching pairs until no pairs remain to be cleared."

      Step-by-Step:
      1. Validation: Fail fast if the string is null or has an odd length (impossible to pair).
      2. Condition: Loop as long as the string actively contains any target substring ("()", "[]", "{}").
      3. Collapse: Re-assign `s` by replacing all occurrences of valid matching pairs with an empty string.
      4. Verify: Check if the final remaining string is empty.

      Why it's an anti-pattern for interviews:
      - Performance Bottleneck: String replacements create entirely new string allocations on the heap.
      - High Time Complexity: Searching and rebuilding the string multiple times yields poor scaling properties.

      Complexity Analysis:
      - Time: O(N^2) -> The loop runs up to N/2 times, and each `.replace()` or `.contains()` scan takes O(N) time.
      - Space: O(N) -> Java strings are immutable. Every replacement creates copies proportional to string size.
    */
    private static boolean isValidParenthesesBrute(String s){
        if(s == null || s.length() %2 != 0)
            return false;

        while(s.contains("()") || s.contains("[]") || s.contains("{}")){
            s = s.replace("()", "")
                 .replace("[]", "")
                 .replace("{}", "");
        }

        // if empty then it means isValid as we replaced all parentheses.
        return s.isEmpty();
    }

     /*
      Approach 2: Optimized (Single Pass with Linear Stack)
      ------------------------------------------------------
      Intuition:
      - Parentheses evaluation follows a Last-In, First-Out (LIFO) recursive nesting pattern.
      - The most recently encountered open bracket *must* match the very next closing bracket seen.
      - A Stack data structure perfectly maps to this tracking requirement.

      Mathematical / Logical Breakdown:
      - Push: When scanning an opening character, store it to track our open expectations.
      - Match: When encountering a closing character, pop the top of the stack and ensure
               it mirrors the incoming closure.
      - Failure: If a closing bracket appears while the stack is empty, or if the popped element
                 does not match, the structural ordering rules are violated.

      Key Idea:
      "Use a LIFO Stack to defer processing open brackets, resolving them immediately against incoming closing bounds."

      Step-by-Step:
      1. Gate Check: Instantly return false if string lengths are odd.
      2. Collection Phase: Initialize a Character Stack to track nesting depths.
      3. Iterative Pass: Loop character-by-character over the sequence.
         - Catch opening frames (`(`, `[`, `{`) and push them onto the stack.
         - Catch closing frames, verify the stack isn't prematurely starved (empty), and pop the head element.
      4. Discrepancy Checks: Validate that the popped open token exactly complements the closed token.
      5. Empty Evaluation: Ensure no dangling, unclosed opening tokens remain stagnant in the stack.

      Visual Example ("([{}])"):
        Iteration 1: '(', Stack: ['(']
        Iteration 2: '[', Stack: ['(', '[']
        Iteration 3: '{', Stack: ['(', '[', '{']
        Iteration 4: '}', Pop '{' -> Matches! Stack: ['(', '[']
        Iteration 5: ']', Pop '[' -> Matches! Stack: ['(']
        Iteration 6: ')', Pop '(' -> Matches! Stack: []
        Final Output (stack.isEmpty()): true

      Complexity Analysis:
      - Time: O(N) -> Single individual pass over the character sequence. Stack push/pop are O(1) ops.
      - Space: O(N) -> Worst-case tracking holds up to N open brackets in memory (e.g. "((((((").
    */
    private static boolean isValidParenthesesOptimized(String s){
        if(s == null || s.length() %2 != 0)
            return false;

        // Stack<Character> stack = new Stack<>(); TRY NOT TO USE STACK IMPLEMENTATION!
        // Preferred over Stack because Stack extends Vector and is synchronized, adding performance overhead.
        // Deque (ArrayDeque) is unsynchronized, faster, and recommended by the official Java docs for pure LIFO operations.
        Deque<Character> stack = new ArrayDeque<>();

        for(char c : s.toCharArray()){
            if(c == '(' || c == '[' || c == '{')
                stack.push(c);
            // handle closing brackets
            else{
                if(stack.isEmpty()) return false;  // as opening brackets for that closing bracket doesn't exist

                char top = stack.pop();
                if(c == ')' && top != '(') return false;
                if(c == ']' && top != '[') return false;
                if(c == '}' && top != '{') return false;
            }
        }

        // if stack is empty that means all brackets were matched perfectly.
        return stack.isEmpty();
    }

}
