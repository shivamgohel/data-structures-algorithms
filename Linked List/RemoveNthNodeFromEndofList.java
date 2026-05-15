// https://leetcode.com/problems/remove-nth-node-from-end-of-list/description/

import java.util.*;

public class RemoveNthNodeFromEndofList {
    // Definition of ListNode
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
            this.next = null;
        }
    }

    public static void main(String[] args) {

        // Approach 1: Brute Force | TC: O(N), SC: O(N)
        ListNode head1 = createList();
        int n1 = 3;
        printList(removeNthFromEndBrute(head1, n1));

        // Approach 2: Two Pass | TC: O(N), SC: O(1)
        ListNode head2 = createList();
        int n2 = 3;
        printList(removeNthFromEndBetter(head2, n2));

        // Approach 3: Optimized One Pass | TC: O(N), SC: O(1)
        ListNode head3 = createList();
        int n3 = 3;
        printList(removeNthFromEndOptimized(head3, n3));
    }

    /*
      Approach 1: Brute Force (ArrayList Storage)
      -------------------------------------------
      Intuition:
      - Store all nodes in an ArrayList to get random access by index.
      - Calculate position from start: length - n
      - Rewire pointers to skip the target node.

      Key Idea:
      "Use ArrayList to convert linked list to array-like structure for easy index access."

      Step-by-Step:
      1. Edge Case: If list has 0 or 1 node, return head (nothing to remove).
      2. Initialize: Create ArrayList<ListNode> to store nodes.
      3. Store: Traverse list once, adding each node to ArrayList.
      4. Calculate: removeIndex = totalNodes - n (0-based index from start)
      5. Remove:
         - If removeIndex == 0: Return head.next (remove first node)
         - Else: Connect node at (removeIndex-1) to node at (removeIndex+1)
      6. Return: Original head (unless head was removed)

      Why this works:
      - ArrayList stores references, so modifying .next affects original list
      - Works for all valid n values (1 to length)

      Complexity:
      - Time: O(N) -> One pass to store nodes
      - Space: O(N) -> Stores all N nodes in ArrayList
    */
    public static ListNode removeNthFromEndBrute(ListNode head, int n){
        if(head == null || head.next == null)
            return head;

        List<ListNode> nodes = new ArrayList<>();
        ListNode temp = head;
        while(temp != null){
            nodes.add(temp);
            temp = temp.next;
        }

        int removeIndex = nodes.size() - n;
        if(removeIndex == 0)
            return head.next;

        nodes.get(removeIndex - 1).next = nodes.get(removeIndex).next;

        return head;
    }

    /*
      Approach 2: Better (Two Pass with Length Calculation)
      TC: O(N) - Two passes through the list (first to count, second to remove)
      SC: O(1) - Only uses a few variables, no extra data structures

      Intuition:
      - First pass: Count total number of nodes
      - Calculate position from start: length - n
      - Second pass: Traverse to node BEFORE target and remove it

      Key Idea:
      "Find length first, then traverse to (length-n-1)th node."

      Step-by-Step:
      1. Edge Case: If list has 0 or 1 node, return null.
      2. First Pass: Count total number of nodes.
      3. Calculate: removeIndex = length - n (0-based position from start)
      4. Handle Special Case: If removeIndex == 0, return head.next
      5. Second Pass: Traverse to node BEFORE target
         - Start at head
         - Move (removeIndex - 1) steps forward
      6. Remove: Connect prev.next to prev.next.next
      7. Return: Original head

      Visual Example:
      List: 1 → 2 → 3 → 4 → 5, n = 2 (remove 4th from end = node 4)
      length = 5, removeIndex = 5 - 2 = 3

      First Pass: Count nodes (5)
      Second Pass: Move to node at index 2 (removeIndex - 1 = 2)
        Start at head (index 0): node1
        Step 1: node2 (index 1)
        Step 2: node3 (index 2) ← prev node
      Remove: node3.next = node5
      Result: 1 → 2 → 3 → 5

      Complexity Analysis:
      - Time: O(N) -> First pass: O(N), Second pass: O(N), Total: O(2N) = O(N)
      - Space: O(1) -> Only uses length, temp, and loop counter variables
    */
    public static ListNode removeNthFromEndBetter(ListNode head, int n){
        if(head == null || head.next == null)
            return null;

        int length = 0;
        ListNode temp = head;
        while(temp != null){
            length++;
            temp = temp.next;
        }

        int removeIndex = length - n;
        if(removeIndex == 0)
            return head.next;

        temp = head;
        // Move (removeIndex - 1) steps to reach node before target
        for(int i=1; i<=removeIndex-1; i++){  // if removeIndex is 6, we have to go 5 steps so 1..2..3..4..5..
            temp = temp.next;
        }

        temp.next = temp.next.next;

        return head;
    }

    /*
      Approach 3: Optimized (One Pass with Two Pointers)

      Intuition:
      - Use two pointers with a fixed gap of n nodes between them
      - When the front pointer reaches the end, the back pointer is exactly at the node BEFORE target
      - This allows removal in a SINGLE pass without knowing length beforehand

      Key Idea:
      "Maintain slow and fast pointers with exactly n nodes between them."

      Step-by-Step:
      1. Edge Case: If list has 0 or 1 node, return null.
      2. Create Dummy Node: Place before head to handle edge cases (removing head)
         dummy.next = head
      3. Initialize: slow = dummy, fast = dummy
      4. Create Gap: Move fast pointer n steps ahead
         Now slow and fast have exactly n nodes between them
      5. Move Together: Advance both pointers until fast reaches the LAST node
         When fast.next == null, slow is exactly at node BEFORE target
      6. Remove: Connect slow.next to slow.next.next (skip the target node)
      7. Return: dummy.next (handles case when original head was removed)

      Visual Example:
      List: 1 → 2 → 3 → 4 → 5, n = 2 (remove 4th from end = node 4)

      Step 1: Create dummy node
        dummy → 1 → 2 → 3 → 4 → 5 → null
        slow,fast

      Step 2: Move fast n = 2 steps ahead
        dummy → 1 → 2 → 3 → 4 → 5 → null
        slow       fast

      Step 3: Move together until fast reaches LAST node (fast.next == null)
        Iteration 1:
          dummy → 1 → 2 → 3 → 4 → 5 → null
              slow       fast

        Iteration 2:
          dummy → 1 → 2 → 3 → 4 → 5 → null
                  slow       fast

        Iteration 3:
          dummy → 1 → 2 → 3 → 4 → 5 → null
                      slow       fast (fast.next == null, stop)

      Step 4: Remove target node (node after slow)
        slow.next = slow.next.next (node3.next = node5)
        Result: dummy → 1 → 2 → 3 → 5 → null

      Return dummy.next: 1 → 2 → 3 → 5

      Why Dummy Node is Important:
      - Without dummy, removing head node requires special case logic
      - Dummy node makes code uniform for all cases (head, middle, tail)
      - dummy.next always points to the correct head after removal
      - Simplifies pointer logic by providing a consistent starting point

      Why This Works (The Math):
      - Let L = length of list
      - Fast moves n steps ahead first
      - Then both move until fast reaches last node (position L-1)
      - Slow moves same distance: from position 0 to position (L-1 - n)
      - So slow ends at node (L-n-1) from start (0-indexed)
      - This is exactly the node BEFORE target (target is at position L-n)
      - When n = L (remove head), slow ends at dummy (position -1), perfect!

      Complexity Analysis:
      - Time: O(N) -> Each node visited exactly once
        * Gap creation: O(n) steps, but n ≤ N
        * Moving together: O(N - n) steps
        * Total: O(N) single pass
      - Space: O(1) -> Only dummy, slow, fast pointers (constant space)
        * No extra data structures
        * No recursion stack
        * Memory usage independent of input size
    */
    public static ListNode removeNthFromEndOptimized(ListNode head, int n){
        if(head == null || head.next == null)
            return null;

        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode slow = dummy;
        ListNode fast = dummy;

        for(int i=0; i<=n; i++)
            fast = fast.next;

        while(fast != null){
            slow = slow.next;
            fast = fast.next;
        }

        slow.next = slow.next.next;

        return dummy.next;
    }

    public static ListNode createList() {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        return head;
    }

    public static void printList(ListNode head) {
        ListNode temp = head;
        while (temp != null) {
            System.out.print(temp.val + " -> ");
            temp = temp.next;
        }
        System.out.println("null");
    }


}
