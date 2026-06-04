// https://leetcode.com/problems/add-two-numbers/description/

import java.math.BigInteger;

public class AddTwoNumbers {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    public static void main(String[] args) {
        /*
      ================================================================================
      PROBLEM STATEMENT:
      You are given two non-empty linked lists representing two non-negative integers.
      The digits are stored in reverse order, and each of their nodes contains a single
      digit. Add the two numbers and return the sum as a linked list.

      You may assume the two numbers do not contain any leading zero, except the
      number 0 itself.

      Constraints:
      - The number of nodes in each linked list is in the range [1, 100].
      - 0 <= Node.val <= 9
      - It is guaranteed that the list represents a number that does not have leading zeros.
      ================================================================================
     */

        ListNode l1 = new ListNode(123);  // 1 2 3
        ListNode l2 = new ListNode(456);  // 6 5 4   sum should be 777

        // Approach 1: Brute Force | TC: O(N + M), SC: O(N + M)
        ListNode result1 = addTwoNumbersBrute(l1, l2);

        // Approach 2: Optimized One Pass | TC: O(max(N, M)), SC: O(1) Auxiliary Space
        ListNode result2 = addTwoNumbersOptimized(l1, l2);
    }

    /*
      Approach 1: Brute Force (String & BigInteger Conversion)
      -------------------------------------------------------
      Intuition:
      - The most literal translation of the problem: extract the sequence of digits,
        reverse them to normal reading order, convert to a real numeric format,
        perform normal math addition, and convert the result back into a new linked list.
      - Standard types like `int` or `long` overflow quickly since lists can span 100 digits,
        making `BigInteger` necessary to avoid accuracy loss.

      Key Idea:
      "Convert linked lists into arbitrary-precision BigInteger strings to aggregate math calculation externally."

      Step-by-Step:
      1. Collect: Traverse `l1` and `l2` independently to accumulate digit characters into separate StringBuilders.
      2. Realign: Reverse both StringBuilders because numbers are originally captured backward (ones-place at the head).
      3. Compute: Parse these strings directly into `BigInteger` instances and use `.add()` to calculate the sum total.
      4. Rebuild: Read the resulting sum string from **right to left** (last index to first index)
         to build the returned deep copy list directly back into the requested reverse order.

      Why it's an anti-pattern for interviews:
      - Object Overheads: String allocations, constant reversals, and BigInteger structures consume heavy heap space.
      - Misses Intent: Avoids direct pointer manipulation, which defeats the point of a core Linked List problem.

      Complexity Analysis:
      - Time: O(N + M) -> Requires separate full iterations to parse out strings and rebuild the ultimate array list.
      - Space: O(N + M) -> Stashes multiple intermediate string representations matching the node counts in memory.
    */
    public static ListNode addTwoNumbersBrute(ListNode l1, ListNode l2){
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        while(l1 != null){
            sb1.append(l1.val);
            l1 = l1.next;
        }
        while(l2 != null){
            sb2.append(l2.val);
            l2 = l2.next;
        }

        BigInteger num1 = new BigInteger(sb1.reverse().toString());
        BigInteger num2 = new BigInteger(sb2.reverse().toString());

        BigInteger sum = num1.add(num2);

        String subStr = sum.toString();
        ListNode dummyHead = new ListNode(-1);
        ListNode curr = dummyHead;

        for(int i=subStr.length()-1; i>=0; i--){
            int digit = Character.getNumericValue(subStr.charAt(i));
            curr.next = new ListNode(digit);

            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
      Approach 2: Optimized (Single Pass with Pointer Math)
      ------------------------------------------------------
      Intuition:
      - Simulates basic elementary school column-addition node-by-node.
      - Because input strings are pre-packaged in reverse order, the ones place, tens place, etc.,
        line up right at the starting heads. No manual list reversal or alignment is needed.

      Mathematical Breakdown:
      - sum:   The grand total of the current column (`val1 + val2 + previous carry`).
               It ranges from 0 up to 19 (e.g., 9 + 9 + 1 carry).
      - digit: The single-digit value for the new node. We extract the ones-place using
               modulo (`sum % 10`). For example, 18 % 10 = 8.
      - carry: The tens-place value passed to the next column. We extract it using integer
               division (`sum / 10`). For example, 18 / 10 = 1.

      Key Idea:
      "Simulate digit-by-digit manual addition in a single pass over both lists, handling carry-overs on-the-fly."

      Step-by-Step:
      1. Create Dummy Node: A placeholder `-1` node anchors our response path to cleanly handle head operations.
      2. Loop condition: Continues while `l1 != null`, `l2 != null`, OR there's a dangling `carry != 0`.
      3. Dynamic Padding: If one list finishes early (variable digit length), substitute its value with 0
         smoothly using inline checks `(list != null) ? list.val : 0`.
      4. Single-Digit Extraction:
         - `sum = val1 + val2 + carry`
         - `digit = sum % 10` (Target value for our new list node)
         - `carry = sum / 10` (Stored over to inject into the subsequent round)
      5. Advance Wisely: Step forward matching active references and attach the new digit nodes.

      Visual Example (Variable Digits: 99 + 1):
        Iteration 1: val1=9, val2=1, carry=0 -> sum=10 -> digit=0, carry=1. List: [-1] -> 0
        Iteration 2: val1=9, val2=0, carry=1 -> sum=10 -> digit=0, carry=1. List: [-1] -> 0 -> 0
        Iteration 3: val1=0, val2=0, carry=1 -> sum=1  -> digit=1, carry=0. List: [-1] -> 0 -> 0 -> 1
        Final Output (dummyHead.next): 0 -> 0 -> 1 (Represents 100)

      Complexity Analysis:
      - Time: O(max(N, M)) -> Steps forward at most the distance of the longest input list plus one extra carry step.
      - Space: O(1) Auxiliary Space -> Keeps zero external collections or strings. Operates entirely in-place.
    */
    public static ListNode addTwoNumbersOptimized(ListNode l1, ListNode l2){
        ListNode dummyHead = new ListNode(-1);
        ListNode curr = dummyHead;
        int carry = 0;

        while(l1 != null || l2 != null || carry != 0){
            int val1 = (l1 != null) ? l1.val : 0;
            int val2 = (l2 != null) ? l2.val : 0;

            int sum = val1 + val2 + carry;
            int digit = sum % 10;
            carry = sum / 10;

            curr.next = new ListNode(digit);
            curr = curr.next;

            if(l1 != null) l1 = l1.next;
            if(l2 != null) l2 = l2.next;
        }

        return dummyHead.next;
    }

}
