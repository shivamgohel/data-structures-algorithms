// https://leetcode.com/problems/reverse-linked-list/description/

import java.util.*;

public class ReverseLinkedList {

    /*
      Problem Statement:
      ------------------
      Given the head of a singly linked list, reverse the list and return the new head.

      Example:
      1 → 2 → 3 → 4  ==>  4 → 3 → 2 → 1
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


    public static void main(String[] args) {

        // linked list: 1 -> 2 -> 3 -> 4
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);

        // TC: O(n), SC: O(n)
        // Approach 1: Brute Force (Stack)
        ListNode head1 = createList();
        head1 = reverseListBrute(head1);
        printList(head1);

        // TC: O(n), SC: O(1)
        // Approach 2: Iterative (3 Pointers - Optimal)
        ListNode head2 = createList();
        head2 = reverseListOptimized1(head2);
        printList(head2);

        // TC: O(n), SC: O(n) recursion stack
        // Approach 3: Recursion
        ListNode head3 = createList();
        head3 = reverseListOptimized2(head3);
        printList(head3);
    }

    /*
      Approach 1: Brute Force (Using Stack)
      ------------------------------------
      Intuition:
      - Store all node values in a stack.
      - Traverse again and overwrite values in reverse order.
      - We do NOT change pointers, only data.

      Step-by-Step:
      1. Traverse list and push values into stack.
      2. Traverse again and pop values back into nodes.

      Complexity:
      - Time: O(n)
      - Space: O(n)
    */
    public static ListNode reverseListBrute(ListNode head){
        if(head == null || head.next == null)
            return head;

        Deque<Integer> stack = new ArrayDeque<>();

        ListNode temp = head;
        while(temp != null){
            stack.push(temp.val);
            temp = temp.next;
        }

        temp = head;
        while(!stack.isEmpty()){
            temp.val = stack.pop();
            temp = temp.next;
        }

        return head;
    }


    /*
      Approach 2: Iterative Pointer Reversal (3 Pointers)
      ---------------------------------------------------

      Intuition:
      - Reverse the linked list by flipping pointers, not values.
      - At each step, we reverse the direction of one link.

      Key Idea:
      "Change arrows, not nodes."

      Step-by-Step Thought Process:
      -----------------------------
      1. Initialize pointers:
         - prev = null
         - curr = head

      2. While curr != null:
         a. Store next node:
            next = curr.next
         b. Reverse link:
            curr.next = prev
         c. Move pointers forward:
            prev = curr
            curr = next

      3. At the end:
         - prev becomes the new head

      Pointer Movement Idea:
      ---------------------
      At every step:
          prev ← curr ← next
          (we shift forward while reversing links backward)


      DRY RUN:
      --------
      Initial List:
          1 → 2 → 3 → 4 → null

      STEP 1:
          prev = null
          curr = 1
          next = 2

          reverse: 1 → null
          move:
              prev = 1
              curr = 2

          Result:
            null ← 1    2 → 3 → 4

      --------------------------------------------------

      STEP 2:
          curr = 2, prev = 1
          next = 3

          reverse: 2 → 1
          move:
              prev = 2
              curr = 3

          Result:
              null ← 1 ← 2   3 → 4

      --------------------------------------------------

      STEP 3:
          curr = 3, prev = 2
          next = 4

          reverse: 3 → 2
          move:
              prev = 3
              curr = 4

          Result:
              null ← 1 ← 2 ← 3   4

      --------------------------------------------------

      STEP 4:
          curr = 4, prev = 3
          next = null

          reverse: 4 → 3
          move:
              prev = 4
              curr = null

          Result:
              null ← 1 ← 2 ← 3 ← 4 (prev which becomes newHead)



      FINAL OUTPUT:
          4 → 3 → 2 → 1 → null


      IMPORTANT OBSERVATION:
      ---------------------
      - prev always becomes new head at the end
      - we never create new nodes
      - only pointer directions are changed


      Complexity:
      ----------
      Time: O(n)
      Space: O(1)
    */
    public static ListNode reverseListOptimized1(ListNode head){
        if(head == null || head.next == null)
            return head;

        ListNode prevNode = null;
        ListNode currNode = head;

        while(currNode != null){
            ListNode nextNode = currNode.next;

            currNode.next = prevNode;

            prevNode = currNode;
            currNode = nextNode;
        }

        return prevNode;
    }


    /*
      Approach 3: Recursion (Reverse Linked List via Backtracking)
      ------------------------------------------------------------

      Intuition:
      - Assume the rest of the list (head.next...) is already reversed.
      - Only fix current node while returning.

      Key Idea:
      "Let recursion reverse the rest, I only attach myself."

      Step-by-Step Thought Process:
      -----------------------------
      1. Go deep until last node (base case → this becomes new head).
      2. While returning from recursion:
         a. Store newHead from deepest call (always same value)
         b. Reverse link:
            - head.next.next = head
         c. Break old forward link:
            - head.next = null
      3. Return newHead upward unchanged.

      Recursion Flow (Call Stack):

          GOING DOWN
          ----------
          reverse(1)
            reverse(2)
              reverse(3)
                reverse(4) → base case (returns 4)

          COMING UP (Unwinding)
          ---------------------
          4 → newHead (fixed)
          3 → attach 3 after 4
          2 → attach 2 after 3
          1 → attach 1 after 2

      Pointer Operation at EACH node:
          head.next.next = head
          head.next = null


      FULL DRY RUN (START TO END):
      ----------------------------

      Initial List:
          1 → 2 → 3 → 4 → null

      STEP 1: Go Down Recursively
      ---------------------------
      reverse(1)
        → calls reverse(2)
          → calls reverse(3)
            → calls reverse(4)
              → base case hit (4.next == null)

      At node 4:
          return 4  → newHead = 4

      ------------------------------------------------------------

      STEP 2: Unwinding starts

      At node 3:
          head = 3, newHead = 4

          1. 4.next = 3        → 4 → 3
          2. 3.next = null

          Result:
              4 → 3 → null

      ------------------------------------------------------------

      At node 2:
          head = 2, newHead = 4

          1. 3.next = 2
          2. 2.next = null

          Result:
              4 → 3 → 2 → null

      ------------------------------------------------------------

      At node 1:
          head = 1, newHead = 4

          1. 2.next = 1
          2. 1.next = null

          Result:
              4 → 3 → 2 → 1 → null

      ------------------------------------------------------------

      FINAL OUTPUT:
          4 → 3 → 2 → 1 → null


      IMPORTANT OBSERVATION:
      ---------------------
      - newHead is ALWAYS 4 because it comes from deepest call and it is NOT recomputed at each step.
      - Only pointer changes happen during return phase
      - No node creates new object, only links are reversed


      Complexity:
      ----------
      Time: O(n)
      Space: O(n) (recursion stack)
    */
    public static ListNode reverseListOptimized2(ListNode head){
        if(head == null || head.next == null)
            return head;

        ListNode newHead = reverseListOptimized2(head.next);

        head.next.next = head;
        head.next = null;

        return newHead;
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
