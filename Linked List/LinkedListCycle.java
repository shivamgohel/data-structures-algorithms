// https://leetcode.com/problems/linked-list-cycle/description/

import java.util.*;

public class LinkedListCycle {

    /*
      Problem Statement:
      ------------------
      Given head, the head of a linked list, determine if the linked list has a cycle in it.
      A cycle exists if there is some node in the list that can be reached again by
      continuously following the next pointer.

      Example:
      Input: 3 → 2 → 0 → -4 ┐
                 ↑_________|
      Output: true (Node -4 points back to Node 2)
    */

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
        ListNode head1 = createList();

        // Approach 1: Brute Force (Hashing) | TC: O(N), SC: O(N)
        System.out.println(hasCycleBrute(head1));

        // Approach 2: Floyd's Cycle-Finding (Standard) | TC: O(N), SC: O(1)
        System.out.println(hasCycleOptimized1(head1));

        // Approach 3: Floyd's Variation (Offset Start) | TC: O(N), SC: O(1)
        System.out.println(hasCycleOptimized2(head1));

    }

     /*
      Approach 1: Brute Force (Hashing / Node Tracking)
      -------------------------------------------------
      Intuition:
      - Imagine walking through a maze. To know if you are walking in circles,
        you can leave a unique mark on every floor tile you step on.
      - If you ever step on a tile that already has your mark, you’ve been there
        before—meaning you are in a loop.

      Key Idea:
      "Store the identity (memory address) of each node in a Set and check for duplicates."

      Step-by-Step:
      1. Edge Case: If the head is null or there's only one node with no loop, return false.
      2. Initialize: Create a HashSet of type <ListNode>.
      3. Traverse: Use a temporary pointer to walk through the list:
         - Before moving to the next node, check: "Is this specific node already in my Set?"
         - If YES: A pointer is pointing back to a previously visited node. Return true (Cycle detected).
         - If NO: Add this node to the Set and move to `temp.next`.
      4. Termination: If `temp` becomes null, we reached the end of a linear list. Return false.

      Why we use ListNode and not ListNode.val:
      - A list can have multiple nodes with the same value (e.g., 1 -> 2 -> 1 -> null).
      - We must store the ACTUAL node object (reference/address) to distinguish
        between different nodes that happen to hold the same data.

      Complexity:
      - Time: O(N) -> We visit every node exactly once.
      - Space: O(N) -> In the worst case (no cycle), we end up storing every node in the HashSet.
    */
    public static boolean hasCycleBrute(ListNode head){
        if(head == null || head.next == null)
            return false;

        HashSet<ListNode> visited = new HashSet<>();
        ListNode temp = head;

        while(temp != null){
            if(visited.contains(temp))
                return true;

            visited.add(temp);
            temp = temp.next;
        }

        return false;
    }

     /*
      Approach 2: Floyd’s Cycle-Finding Algorithm (Tortoise and Hare)
      ---------------------------------------------------------------
      Intuition:
      - Imagine two runners on a track. One runs slow (Tortoise) and the other
        runs twice as fast (Hare).
      - If the track is a straight line, the Hare will reach the finish line
        and the Tortoise will never see him again.
      - If the track is circular, the Hare will eventually "lap" the Tortoise
        and they will inevitably collide at some point.

      Key Idea:
      "Use two pointers moving at different speeds to detect a loop without extra memory."

      Step-by-Step:
      1. Edge Case: If the head is null or there is only one node, no cycle is possible.
      2. Initialize:
         - `slow` pointer starts at the head.
         - `fast` pointer starts at the head.
      3. Traverse: Use a while loop that runs as long as `fast` and `fast.next` are not null:
         - Move `slow` by exactly one node (`slow = slow.next`).
         - Move `fast` by exactly two nodes (`fast = fast.next.next`).
         - After every move, check: "Is the slow pointer at the same memory address as the fast pointer?"
         - If YES: The pointers have collided in the loop. Return true.
      4. Termination: If the loop ends because `fast` hit null, the list is linear. Return false.

      Why this works (The Mathematical Gap):
      - Think of the distance between Fast and Slow inside a cycle as a "Gap".
      - If the cycle length is 10 and Fast is at position 7 while Slow is at position 2, the gap is 5.
      - In the next step:
          - Slow moves to 3 (position + 1).
          - Fast moves to 9 (position + 2).
          - The new gap is 6? No! Because they are in a circle, Fast is actually
            approaching Slow from behind.
      - Every single step, Fast closes the distance by exactly 1 node ($2 - 1 = 1$).
      - Example: If the gap is 5 nodes:
          - Step 1: Gap becomes 4.
          - Step 2: Gap becomes 3.
          - Step 3: Gap becomes 2.
          - Step 4: Gap becomes 1.
          - Step 5: Gap becomes 0 (Collision!).
      - Because the gap decreases by 1 every time, they are guaranteed to meet
        and Fast can never "jump over" Slow without hitting him.

      Complexity:
      - Time: O(N) -> Fast catches Slow in at most one full lap of the cycle.
      - Space: O(1) -> No extra data structures used, only two pointer variables.
    */
    public static boolean hasCycleOptimized1(ListNode head){
        if(head == null || head.next == null)
            return false;

        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast)
                return true;
        }

        return false;
    }

     /*
      Approach 3: Floyd’s Variation (Offset Start / "While-Not-Equal")
      ---------------------------------------------------------------
      Intuition:
      - This is the same "Tortoise and Hare" concept as Approach 2, but we
        intentionally start the Hare one step ahead of the Tortoise.
      - In Approach 2, because they start at the same spot, we have to move
        them *before* we can check if they are equal.
      - In Approach 3, starting them at different spots allows us to use the
        collision itself as the loop's exit condition, making the code read
        more like a natural sentence: "Keep moving while they haven't met."

      Key Idea:
      "Start pointers at different positions to simplify the loop condition."

      Step-by-Step:
      1. Edge Case: Check if `head` or `head.next` is null. If so, return false
         (can't have a cycle with fewer than 2 nodes in this logic).
      2. Initialize:
         - `slow` starts at `head`.
         - `fast` starts at `head.next` (The Offset).
      3. Traverse: Use a while loop: `while (slow != fast)`
         - Safety Check: Inside the loop, immediately check if `fast` or
           `fast.next` is null. If it is, the Hare reached the end. Return false.
         - Move `slow` by 1 node.
         - Move `fast` by 2 nodes.
      4. Collision: If the loop exits, it means `slow == fast` became true.
         A cycle exists! Return true.

      Why this is "Cleaner":
      - It eliminates the `if(slow == fast)` check inside the loop body.
      - It treats the "meeting point" as the final destination of the loop.

      The Mathematical Gap (Offset Version):
      - In Approach 2, the initial gap was 0 (then increased to $C-1$ after the 1st move).
      - In Approach 3, the initial gap is exactly 1 (Fast is ahead).
      - If the cycle length is $C$, Fast is actually $C-1$ nodes *behind* Slow.
      - Just like Approach 2, every step reduces that $C-1$ gap by exactly 1
        until it reaches 0.

      Complexity:
      - Time: O(N) -> Still linear, as the Hare still laps the Tortoise.
      - Space: O(1) -> No extra memory allocation.
    */
    public static boolean hasCycleOptimized2(ListNode head){
        if(head == null || head.next == null)
            return false;

        ListNode slow = head;
        ListNode fast = head.next;

        while(slow != fast){
            // fast reaches end that means there was no cycle!
            if(fast == null || fast.next == null)
                return false;

            slow = slow.next;
            fast = fast.next.next;
        }

        // while loop breaks when slow == fast, which means there was cycle!
        return true;
    }

    public static ListNode createList(){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        return head;
    }

}
