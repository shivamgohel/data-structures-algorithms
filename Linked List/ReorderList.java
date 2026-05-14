// https://leetcode.com/problems/reorder-list/description/

import java.util.*;

public class ReorderList {

    // Definition of ListNode
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val){
            this.val = val;
            this.next = null;
        }
    }

    static void main(String[] args) {

        // Approach 1: Brute Force (ArrayList) | TC: O(N), SC: O(N)
        ListNode head1 = createList();
        reorderBrute(head1);

        // Approach 2: Optimized (Find Middle + Reverse + Merge) | TC: O(N), SC: O(1)
        ListNode head2 = createList();
        reorderOptimized(head2);
    }

    /*
      Approach 1: Brute Force (ArrayList Storage)
      -------------------------------------------
      Intuition:
      - Imagine you have a line of people. You want to rearrange them as:
        Person0, PersonLast, Person1, PersonSecondLast, Person2...
      - Without breaking the chain, it's hard to jump to the end repeatedly.
      - So you write down all their positions on paper first (store in array).
      - Then you just connect them according to your paper plan.

      Key Idea:
      "Store the identity (memory address) of each node in an ArrayList
       and rewire pointers using two-pointer technique."

      Step-by-Step:
      1. Edge Case: If head is null or there's only one node, no reordering needed.
      2. Initialize: Create an ArrayList of type <ListNode>.
      3. Store: Traverse the list once, adding each node to the ArrayList.
      4. Reorder: Use left pointer at start (0) and right pointer at end (n-1):
         - Connect node[left] to node[right]
         - Move left forward by 1
         - If left == right, break (middle element reached)
         - Connect node[right] to node[left]
         - Move right backward by 1
      5. Terminate: When loop ends, set the next of the middle node to null.
         (This prevents cycles and properly ends the list.)

      Why we store the actual ListNode objects:
      - We need to modify the 'next' pointers directly.
      - Storing references allows us to rewire connections O(1) per step.
      - Values can repeat, so we must store node references, not values.

      Visual Example:
      Original List: 1 → 2 → 3 → 4 → 5
      ArrayList: [node1, node2, node3, node4, node5]

      Iteration 1: left=0, right=4
        node1.next = node5  → 1 → 5
        left becomes 1
        node5.next = node2  → 5 → 2
        right becomes 3

      Iteration 2: left=1, right=3
        node2.next = node4  → 2 → 4
        left becomes 2 (left == right, break)

      Final: node2.next = null
      Result: 1 → 5 → 2 → 4 → 3 → null

      Complexity:
      - Time: O(N) -> We visit every node exactly once to store, once to rewire.
      - Space: O(N) -> In worst case (no cycle), we store every node in ArrayList.
    */
    public static void reorderBrute(ListNode head){
        if(head == null || head.next == null)
            return;

        List<ListNode> nodes = new ArrayList<>();
        ListNode temp = head;
        while(temp != null){
            nodes.add(temp);
            temp = temp.next;
        }

        int left = 0;
        int right = nodes.size() - 1;
        while(left < right){
            nodes.get(left).next = nodes.get(right);
            left++;

            if(left == right) break;

            nodes.get(right).next = nodes.get(left);
            right--;
        }

        nodes.get(left).next = null;
        
        printList(head);
    }

    /*
      Approach 2: Optimized (Find Middle + Reverse + Merge)
      ------------------------------------------------------
      Intuition:
      - Look at target pattern: 1 → 5 → 2 → 4 → 3
      - Observe: First half (1,2,3) interleaved with REVERSED second half (5,4)
      - So problem decomposes into three simple steps:
        1) Split list at middle
        2) Reverse second half
        3) Merge alternatingly

      Key Idea:
      "Use slow/fast pointer to find middle (just like cycle detection),
       reverse the second half iteratively, then weave them together."

      Step-by-Step:
      1. Edge Case: If head is null or there's only one node, no reordering needed.
      2. Find Middle: Use slow (1 step) and fast (2 steps) pointers.
         When fast reaches end, slow is at middle.
      3. Split List: Store second half head in 'second' variable,
         then set slow.next = null to break the list.
      4. Reverse Second Half: Standard iterative reversal using prev pointer.
      5. Merge Alternatingly: Take one node from first half, then one from
         reversed second half, alternating until second half is exhausted.

      Complexity:
      - Time: O(N) -> Each phase is O(N), total 3 passes through the list.
        Phase 1: Find middle - O(N/2)
        Phase 2: Reverse second half - O(N/2)
        Phase 3: Merge - O(N/2)
        Total: O(N)
      - Space: O(1) -> Only use a few pointer variables, no extra data structures.

      ============ DRY RUN FOR ODD LENGTH (5 NODES) ============
      Input: 1 → 2 → 3 → 4 → 5 → null

      PHASE 1 - FIND MIDDLE:
      -----------------------
      Initial: slow=1, fast=1

      Iteration 1: fast.next=2, fast.next.next=3 (both not null)
        slow = slow.next → slow=2
        fast = fast.next.next → fast=3

      Iteration 2: fast.next=4, fast.next.next=5 (both not null)
        slow = slow.next → slow=3
        fast = fast.next.next → fast=5

      Iteration 3: fast.next=null (condition fails)
        Exit loop

      Result: slow=3 (middle node), fast=5 (last node)

      PHASE 2 - SPLIT AND REVERSE SECOND HALF:
      -----------------------------------------
      Before split:
        First half (still connected): 1 → 2 → 3 → 4 → 5
        second = slow.next = 3.next = node4

      Split: slow.next = null
        First half: 1 → 2 → 3 → null
        Second half: 4 → 5 → null

      Reverse second half:
        Initial: prev=null, second=4→5→null

        Iteration 1: second=4
          nextNode = second.next = 5
          second.next = prev = null
          prev = second = 4
          second = nextNode = 5
          List now: 4→null, 5→null (detached)

        Iteration 2: second=5
          nextNode = second.next = null
          second.next = prev = 4
          prev = second = 5
          second = nextNode = null
          List now: 5→4→null

      After reversal: second=prev=5→4→null

      PHASE 3 - MERGE ALTERNATINGLY:
      ------------------------------
      Initial: first=1→2→3→null, second=5→4→null

      Iteration 1 (second != null):
        firstNext = first.next = 2
        secondNext = second.next = 4

        Rewire: first.next = second → 1→5
               second.next = firstNext → 5→2
        Result: 1→5→2→3→null

        Move: first = firstNext = 2
              second = secondNext = 4

      Iteration 2 (second != null):
        firstNext = first.next = 3
        secondNext = second.next = null

        Rewire: first.next = second → 2→4
               second.next = firstNext → 4→3
        Result: 2→4→3→null

        Move: first = firstNext = 3
              second = secondNext = null

      Loop ends (second == null)

      Final list: 1→5→2→4→3→null

      ============ DRY RUN FOR EVEN LENGTH (4 NODES) ============
      Input: 1 → 2 → 3 → 4 → null

      PHASE 1 - FIND MIDDLE:
      -----------------------
      Initial: slow=1, fast=1

      Iteration 1: fast.next=2, fast.next.next=3 (both not null)
        slow = 2
        fast = 3

      Iteration 2: fast.next=4, fast.next.next=null (second condition fails)
        Exit loop

      Result: slow=2 (first of two middle nodes), fast=3

      PHASE 2 - SPLIT AND REVERSE SECOND HALF:
      -----------------------------------------
      second = slow.next = 2.next = node3
      slow.next = null
        First half: 1→2→null
        Second half: 3→4→null

      Reverse second half:
        Iteration 1: second=3→4→null
          nextNode=4, 3.next=null, prev=3, second=4
        Iteration 2: second=4→null
          nextNode=null, 4.next=3, prev=4, second=null

      Result: second=4→3→null

      PHASE 3 - MERGE:
      -----------------
      Initial: first=1→2→null, second=4→3→null

      Iteration 1:
        firstNext=2, secondNext=3
        1.next=4, 4.next=2
        first=2, second=3
        Result so far: 1→4→2→null

      Iteration 2:
        firstNext=null, secondNext=null
        2.next=3, 3.next=null
        first=null, second=null

      Final list: 1→4→2→3→null
    */
    public static void reorderOptimized(ListNode head){
        if(head == null || head.next == null)
            return;

        // 1) find the middle
        ListNode slow = head;
        ListNode fast = head;
        while(fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2) reverse the second half
        ListNode second = slow.next;
        slow.next = null;
        // reversing the second half
        ListNode prev = null;
        while(second != null){
            ListNode nextNode = second.next;
            second.next = prev;
            prev = second;
            second = nextNode;
        }
        second = prev;

        // 3) merge alternatingly
        ListNode first = head;
        while(second != null){
            ListNode firstNext = first.next;
            ListNode secondNext = second.next;

            first.next = second;
            second.next = firstNext;

            first = firstNext;
            second = secondNext;
        }

        printList(head);
    }

    public static ListNode createList(){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        return head;
    }

    public static void printList(ListNode head){
        ListNode temp = head;
        while(temp != null){
            System.out.print(temp.val + " -> ");
            temp = temp.next;
        }
        System.out.println("null");
    }
    
}
