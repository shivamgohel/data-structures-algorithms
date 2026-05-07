// https://leetcode.com/problems/merge-two-sorted-lists/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeTwoSortedLists {

    /*
      Problem Statement:
      ------------------
      Merge two sorted linked lists and return it as a sorted list.
      The list should be made by splicing together the nodes of the first two lists.

      Example:
      L1: 1 → 2 → 4
      L2: 1 → 3 → 4
      Output: 1 → 1 → 2 → 3 → 4 → 4
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


        // TC: O((n+m) log(n+m)), SC: O(n+m)
        // Approach 1: Brute Force (Store in List, Sort that List and make new Linked List)
        ListNode head1 = createList();
        ListNode head2 = createList();
        ListNode mergedHead1 = mergeTwoSortedListBrute(head1, head2);
        printList(mergedHead1);

        // TC: O(n+m), SC: O(n+m) recursion stack
        // Approach 2: Recursive
        head1 = createList();
        head2 = createList();
        ListNode mergedHead2 = mergeTwoSortedListOptimized1(head1, head2);
        printList(mergedHead2);

        // TC: O(n+m), SC: O(1)
        // Approach 3: Iterative (Two-Pointer - Optimal)
        head1 = createList();
        head2 = createList();
        ListNode mergedHead3 = mergeTwoSortedListOptimized2(head1, head2);
        printList(mergedHead3);

    }

    /*
      Approach 1: Brute Force (Using ArrayList)
      -----------------------------------------
      Intuition:
      - Copy all elements from both Linked Lists into an ArrayList.
      - Use built-in sort to sort the values.
      - Reconstruct a brand new Linked List from the sorted values.

      Complexity:
      - Time: O((n+m) log(n+m)) due to sorting.
      - Space: O(n+m) for the list and new nodes.
    */
    private static ListNode mergeTwoSortedListBrute(ListNode head1, ListNode head2){
        if(head1 == null) return head2;
        if(head2 == null) return head1;

        List<Integer> mergedList = new ArrayList<>();

        ListNode temp = head1;
        while(temp != null){
            mergedList.add(temp.val);
            temp = temp.next;
        }
        temp = head2;
        while(temp != null){
            mergedList.add(temp.val);
            temp = temp.next;
        }

        Collections.sort(mergedList);

        ListNode dummyHead = new ListNode(-1);
        ListNode dummyTail = dummyHead;

        for(int sortedValues : mergedList){
            dummyTail.next = new ListNode(sortedValues);
            dummyTail = dummyTail.next;
        }

        return dummyHead.next;
    }

    /*
      Approach 2: Recursive Merge
      ---------------------------
      Intuition:
      - A sorted merge is just picking the smaller head and attaching it
        to the result of merging the remaining nodes.
      - We treat the Linked List as a recursive structure: a Head followed by a Tail (which is also a list).

      Key Idea:
      "Pick the winner, then delegate the next link to recursion."

      Step-by-Step:
      1. Base Case: If head1 is null, return head2. If head2 is null, return head1.
      2. Decide: Which head is smaller?
      3. If head1 is smaller:
         - head1.next = merge(head1.next, head2) // "Find my next neighbor"
         - return head1 // "Pass myself back to my previous neighbor"
      4. If head2 is smaller:
         - head2.next = merge(head1, head2.next)
         - return head2

      RECURSIVE STACK & DRY RUN:
      --------------------------
      Example: L1: 1→3, L2: 2→4

      1. merge(1, 2): 1 < 2. Winner: [1]. Calls merge(3, 2)
         2. merge(3, 2): 2 < 3. Winner: [2]. Calls merge(3, 4)
            3. merge(3, 4): 3 < 4. Winner: [3]. Calls merge(null, 4)
               4. merge(null, 4): Base Case! Returns [4]

      UNWINDING (Stitching):
      3. merge(3, 4) received [4]. Sets 3.next = 4. Returns [3]
      2. merge(3, 2) received [3]. Sets 2.next = 3. Returns [2]
      1. merge(1, 2) received [2]. Sets 1.next = 2. Returns [1] (Final Head)

      Complexity:
      - Time: O(n+m) -> We visit every node exactly once.
      - Space: O(n+m) -> Every comparison adds a new frame to the Call Stack.
    */
    private static ListNode mergeTwoSortedListOptimized1(ListNode head1, ListNode head2){
        if(head1 == null) return head2;
        if(head2 == null) return head1;

        if(head1.val <= head2.val){
            head1.next = mergeTwoSortedListOptimized1(head1.next, head2);
            return head1;
        }else{
            head2.next = mergeTwoSortedListOptimized1(head1, head2.next);
            return head2;
        }
    }

    /*
      Approach 3: Iterative (Two-Pointer - Optimal)
      --------------------------------------------
      Intuition:
      - Instead of using the call stack, we use a 'while' loop to manually
        traverse and rewire the existing nodes.
      - We use a 'dummy' node to act as a permanent anchor for the head.

      Key Idea:
      "Use a tail pointer to sew the two lists together like a zipper."

      Step-by-Step:
      1. Create a dummy node (val: -1).
      2. Set a 'tail' pointer to the dummy node.
      3. While both head1 and head2 are not null:
         - Compare head1.val and head2.val.
         - Attach the smaller node to tail.next.
         - Move the head of the chosen list forward.
         - Move the tail forward.
      4. Final Stitch: If one list is empty, attach the remainder of the other list.
      5. Return dummy.next.

      DRY RUN:
      --------
      Initial: L1: 1→3, L2: 2→4 | Dummy(-1), Tail points to Dummy

      Iteration 1: Compare 1 and 2.
                   1 is smaller. Tail.next = [1].
                   Move L1 to 3. Move Tail to [1].
                   Result so far: Dummy → 1

      Iteration 2: Compare 3 and 2.
                   2 is smaller. Tail.next = [2].
                   Move L2 to 4. Move Tail to [2].
                   Result so far: Dummy → 1 → 2

      Iteration 3: Compare 3 and 4.
                   3 is smaller. Tail.next = [3].
                   Move L1 to null. Move Tail to [3].
                   Result so far: Dummy → 1 → 2 → 3

      Final Stitch: L1 is null. Attach remainder of L2 ([4]).
                    Tail.next = [4].
                    Final Result: Dummy → 1 → 2 → 3 → 4

      Complexity:
      ----------
      Time: O(n+m) -> We visit each node exactly once.
      Space: O(1)  -> No recursion stack, no extra lists. Just a few pointers.
    */
    private static ListNode mergeTwoSortedListOptimized2(ListNode head1, ListNode head2){
        if(head1 == null) return head2;
        if(head2 == null) return head1;

        ListNode dummyHead = new ListNode(-1);
        ListNode dummyTail = dummyHead;

        while(head1 != null && head2 != null){
            if(head1.val <= head2.val){
                dummyTail.next = head1;
                dummyTail = dummyTail.next;
                head1 = head1.next;
            }else{
                dummyTail.next = head2;
                dummyTail = dummyTail.next;
                head2 = head2.next;
            }
        }

        if(head1 != null)
            dummyTail.next = head1;
        if(head2 != null)
            dummyTail.next = head2;

        return dummyHead.next;
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
