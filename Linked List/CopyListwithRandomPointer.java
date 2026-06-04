// https://leetcode.com/problems/copy-list-with-random-pointer/description/

import java.util.*;

/*
Problem Statement:
  ------------------
  A linked list of length n is given such that each node contains an additional random
  pointer, which could point to any node in the list, or null.

  Construct a deep copy of the list. The deep copy should consist of exactly n brand
  new nodes, where each new node has its value set to the value of its corresponding
  original node. Both the next and random pointers of the new nodes should point to
  new nodes in the copied list such that the pointers in the original list and copied
  list represent the same list state. None of the pointers in the new list should
  point to nodes in the original list.

  For example, if there are two nodes X and Y in the original list where X.random --> Y,
  then for the corresponding two nodes x and y in the copied list, x.random --> y.

  Return the head of the copied linked list.

  Constraints:
  ------------
  - 0 <= n <= 1000
  - -10^4 <= Node.val <= 10^4
  - Node.random is null or is pointing to some node in the linked list.
*/

public class CopyListwithRandomPointer {
    // Definition for a Node.
    static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    static void main(String[] args) {

        // Approach 1: Brute Force | TC: O(N), SC: O(N)
        Node head1 = createSampleList();
        printList(head1);
        printList(copyRandomListBrute(head1));

        // Approach 2: Optimized In-Place | TC: O(N), SC: O(1)
        Node head2 = createSampleList();
        printList(head2);
        printList(copyRandomListOptimized(head2));
    }

    /*
      Approach 1: Brute Force (HashMap Mapping)
      -----------------------------------------
      Intuition:
      - Deep copying a linked list with random pointers is tricky because target
        nodes of random pointers might not be created yet during a single pass.
      - Using a HashMap allows us to map each original node to its respective cloned copy.
      - Once all copies are registered in the map, a second pass sets up the connections.

      Key Idea:
      "Map original nodes to cloned copies to enable O(1) random access pointer wiring."

      Step-by-Step:
      1. Edge Case: If the head is null, return null immediately.
      2. First Pass: Traverse the original list node-by-node. For each node, create an independent
         clone with the same value and put the pair (originalNode, clonedNode) into a HashMap.
      3. Second Pass: Traverse the original list again. For every original node:
         - Retrieve its matching cloned node from the map.
         - Set `clonedNode.next` to the clone of `originalNode.next`.
         - Set `clonedNode.random` to the clone of `originalNode.random`.
      4. Return: The clone corresponding to the original head.

      Why this works:
      - The map decouples node allocation from relationship linking.
      - Safely handles `null` references naturally because `map.get(null)` returns `null`.

      Complexity Analysis:
      - Time: O(N) -> Two distinct passes over the list of size N.
      - Space: O(N) -> Map scales linearly to store mapping keys and values for N nodes.
    */
    public static Node copyRandomListBrute(Node head){
        if(head == null)
            return null;

        // {originalNode, copiedNode}
        Map<Node, Node> oldToNewMap = new HashMap<>();

        Node originalNode = head;
        while(originalNode != null){
            Node copiedNode = new Node(originalNode.val);
            oldToNewMap.put(originalNode, copiedNode);

            originalNode = originalNode.next;
        }

        originalNode = head;
        while(originalNode != null){
            Node copiedNode = oldToNewMap.get(originalNode);

            copiedNode.next = oldToNewMap.get(originalNode.next);
            copiedNode.random = oldToNewMap.get(originalNode.random);

            originalNode = originalNode.next;
        }

        return oldToNewMap.get(head);
    }

    /*
      Approach 2: Optimized (In-Place Node Interweaving)
      --------------------------------------------------
      Intuition:
      - To optimize extra space down to O(1), we discard the HashMap.
      - Instead, we embed each cloned node directly next to its corresponding original node.
      - Because of this side-by-side positioning, the clone of any node `X` is always `X.next`.

      Key Idea:
      "Weave cloned nodes directly into the original list structure to maintain reference links without space overhead."

      Step-by-Step:
      1. Step 1 (Interweave): Iterate through the list. Create a clone node for each node,
         and insert it directly between the current node and the current node's next node.
         Original: A -> B -> C
         Interleaved: A -> A' -> B -> B' -> C -> C'
      2. Step 2 (Assign Randoms): Iterate through the woven list using the original nodes.
         If an original node has a random pointer (`curr.random != null`), then its clone's random
         pointer (`curr.next.random`) must point to the clone of the target (`curr.random.next`).
      3. Step 3 (Separate): Detangle the interwoven list into two independent lists.
         Restore original list links while chaining together clone nodes to construct the deep copy.
      4. Return: The head of the newly extracted deep copy list.

      Visual Example:
      Original: [7, r->null] -> [13, r->7] -> null

      Step 1 (Interweave):
        [7] -> [7'] -> [13] -> [13'] -> null

      Step 2 (Assign Randoms):
        7.random is null -> 7'.random = null
        13.random is 7 -> 13'.random = 13.random.next = 7'

      Step 3 (Separate):
        Original Restored: [7, r->null] -> [13, r->7] -> null
        Cloned Extracted:  [7', r->null] -> [13', r->7'] -> null

      Complexity Analysis:
      - Time: O(N) -> Three passes over a list size proportional to N (O(3N) total).
      - Space: O(1) -> Modifies existing pointers in-place; no auxiliary global collection memory.
    */
    private static Node copyRandomListOptimized(Node head){
        if(head == null)
            return null;

        Node curr = head;
        while(curr != null){
            Node next = curr.next;
            Node copy = new Node(curr.val);

            curr.next = copy;
            copy.next = next;

            curr = next;
        }

        curr = head;
        while(curr != null){
            if(curr.random != null)
                curr.next.random = curr.random.next;

            curr = curr.next.next;
        }

        curr = head;
        Node dummyHead = new Node(-1);
        Node copyCurr = dummyHead;

        while(curr != null){
            Node nextOriginal = curr.next.next;

            // extract the clone
            copyCurr.next = curr.next;
            copyCurr = copyCurr.next;

            // restore the original LL
            curr.next = nextOriginal;

            curr = nextOriginal;
        }
        
        return  dummyHead.next;
    }


    // Creates the LeetCode example list: [[7,null],[13,0],[11,4],[10,2],[1,0]]
    public static Node createSampleList() {
        Node node1 = new Node(7);
        Node node2 = new Node(13);
        Node node3 = new Node(11);
        Node node4 = new Node(10);
        Node node5 = new Node(1);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        node1.random = null;
        node2.random = node1;
        node3.random = node5;
        node4.random = node3;
        node5.random = node1;

        return node1; // Returns the head
    }

    public static void printList(Node head) {
        if (head == null) {
            System.out.println("null");
            return;
        }
        Node curr = head;
        while (curr != null) {
            String randomVal = (curr.random != null) ? String.valueOf(curr.random.val) : "null";
            System.out.print("[" + curr.val + ", r->" + randomVal + "] -> ");
            curr = curr.next;
        }
        System.out.println("null");
    }
}
