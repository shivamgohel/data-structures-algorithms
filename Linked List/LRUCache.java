import java.net.Inet4Address;
import java.util.*;

/*
      ================================================================================
      PROBLEM STATEMENT:
      Design a data structure that follows the constraints of a Least Recently Used
      (LRU) cache.

      Implement the LRUCache class:
      - LRUCache(int capacity): Initialize the LRU cache with positive size capacity.
      - int get(int key): Return the value of the key if the key exists, otherwise return -1.
      - void put(int key, int value): Update the value of the key if the key exists.
        Otherwise, add the key-value pair to the cache. If the number of keys exceeds
        the capacity from this operation, evict the least recently used key.

      Constraints:
      - 1 <= capacity <= 3000
      - 0 <= key <= 10^4
      - 0 <= value <= 10^5
      - At most 2 * 10^5 calls will be made to get and put.
      - The functions get and put must each run in O(1) average time complexity.
      ================================================================================
 */

/*
  Approach 1: Brute Force (Scan & Shift / Linear Array Pull)
  -------------------------------------------------------
  Intuition:
  - Linearly scan the collection to locate the target key node.
  - Because a standard ArrayList lacks index-mapping capabilities, we must look at
    elements one by one using an iterative search frame.
  - Once found, the element must be extracted and relocated to maintain chronological access.

  Key Idea:
  - "Locate the key via structural linear inspection, then move it to the tail to
     designate it as the Most Recently Used element."

  Step-by-Step:
  1. Linear Scan: Iterate through the active list from index 0 up to its current size.
  2. Key Verification: Extract the array block and check if `cache.get(i)[0]` matches the key.
  3. Priority Refresh: Remove the matching pair from its current slot and append it to the tail.
  4. Return Payload: Yield the value stored at index `[1]` of the extracted array block.
  5. Fallback: Return -1 if the loop completes without finding a matching key identifier.

  Why it's an anti-pattern for interviews:
  - Extreme scaling bottlenecks. Removing an item from the middle of an ArrayList forces
    Java to physically shift all subsequent elements left in memory to patch the gap. This
    violates the $O(1)$ time complexity mandate and causes Time Limit Exceeded (TLE) errors.

  Complexity Analysis:
  - Time: O(N) -> Sequential searching takes O(N), and internal element shifting takes O(N).
  - Space: O(N) ->
*/
/*
public class LRUCache   {
    private ArrayList<int[]> cache;
    private int capacity;

    public LRUCache(int capacity){
        this.cache = new ArrayList<>();
        this.capacity = capacity;
    }

    public int get(int key){
        for(int i=0; i<cache.size(); i++){
            if(cache.get(i)[0] == key){
                int[] temp = cache.remove(i);
                cache.add(temp);
                return temp[1];
            }
        }

        return -1;
    }

    public void put(int key, int value){
        for(int i=0; i<cache.size(); i++){
            if(cache.get(i)[0] == key){
                int[] temp = cache.remove(i);
                temp[1] = value;
                cache.add(temp);
                return;
            }
        }

        if(capacity == cache.size())
            cache.remove(0);

        cache.add(new int[]{key, value});
    }

}
*/

/*
  Approach 2: Optimal Design (HashMap + Doubly Linked List)
  ---------------------------------------------------------
  Intuition:
  - To achieve O(1) time complexity for both read and write operations, we need a data
    structure that provides O(1) lookups and O(1) structural reordering (insertion/deletion).
  - A standard HashMap gives us O(1) lookups but has no concept of element ordering.
  - A Doubly Linked List (DLL) allows us to remove and insert nodes in O(1) time *if*
    we already have a reference to the node.
  - Combining them—where the HashMap stores keys mapping directly to DLL Nodes—unlocks
    constant-time performance for all operations.

  Key Idea:
  - "Use a HashMap for instant node addressing, paired with a Doubly Linked List using
     dummy Head and Tail bounds to seamlessly shuffle data priorities in constant time."

  Core Architecture Components:
  1. Dummy Head & Tail Nodes: Act as permanent boundary anchors.
     - Nodes near `head.next` represent the Most Recently Used (MRU) elements.
     - Nodes near `tail.prev` represent the Least Recently Used (LRU) elements.
     - Eliminates edge-case null pointer checks during list mutation.
  2. Map Storage: Tracks `Map<Integer, Node>`, mapping explicit integer keys to
     their live physical memory references inside the DLL.

  Step-by-Step Mechanics:
  - get(key):
    1. Check map existence. If missing, return -1.
    2. Extract the corresponding `Node` tracking that key.
    3. Sever the node from its current spot (`removeNode`) and re-splice it immediately
       after the dummy head (`addNode`), marking it as Most Recently Used.
    4. Return `node.value`.

  - put(key, value):
    1. If the key exists: Update its inner value and bubble it up to the head (`moveToHead`).
    2. If the key is brand new:
       a. Check capacity constraint (`map.size() >= capacity`).
       b. If full, locate the victim at `tail.prev` (LRU node), break its link chains
          (`removeNode`), and purge its key footprint entirely from the map.
       c. Instantiate a brand new `Node(key, value)`.
       d. Insert it at the front of the DLL (`addNode`) and register it in the map.

  Complexity Analysis:
  - Time: O(1) -> Map lookups, node splicing, and node deletions happen purely via reference swapping.
  - Space: O(N) -> Linear memory proportional to the maximum allowed capacity, scaling up to N elements.
*/
/*public class LRUCache   {
    private class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(){}
        Node(int key, int value){
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> map;
    private Node head;
    private Node tail;

    public LRUCache(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();

        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key){
        if(!map.containsKey(key))
            return -1;

        Node node = map.get(key);
        moveToHead(node);

        return node.value;
    }

    public void put(int key, int value){
        if(map.containsKey(key)){
            Node node = map.get(key);
            node.value = value;
            moveToHead(node);
        }else{
            if(map.size() >= capacity){
                Node lru = tail.prev;
                removeNode(lru);
                map.remove(lru.key);
            }

            Node newNode = new Node(key, value);
            addNode(newNode);
            map.put(key, newNode);
        }
    }

    private void removeNode(Node node){
        Node prevNode = node.prev;
        Node nextNode = node.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    private void addNode(Node node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void moveToHead(Node node){
        removeNode(node);
        addNode(node);
    }

}*/

/*
  Approach 3: Java Standard Library Leverage (LinkedHashMap)
  -----------------------------------------------------------
  Intuition:
  - Instead of manually writing a Doubly Linked List and wired pointer logic, we can
    use Java's native `LinkedHashMap`.
  - A standard LinkedHashMap maintains a doubly-linked list running through all of its
    entries, which by default governs insertion order.

  Key Magic Hooks:
  1. The `accessOrder` Flag:
     - The third parameter in the constructor `new LinkedHashMap<>(capacity, 0.75f, true)`
       tells Java to order the elements by **access order** instead of insertion order.
     - Every time `.get()` or `.put()` is invoked, Java automatically shifts that node to
       the end of its internal linked list (marking it as Most Recently Used).
  2. The `removeEldestEntry()` Override:
     - This lifecycle hook is automatically evaluated by Java after every `.put()` operation.
     - By returning `size() > capacity`, we tell the map: "If we just exceeded our size
       limit, automatically evict the oldest node (the LRU entry at the head of the list)."

  Complexity Analysis:
  - Time: O(1) -> Built-in hash lookups and native pointer manipulation run at constant time.
  - Space: O(N) -> Memory scales linearly with the cache capacity constraint.
*/
public class LRUCache   {

    private Map<Integer, Integer> cache;
    private final int capacity;

    public LRUCache(int capacity){
        this.capacity = capacity;

        // Arguments: (initialCapacity, loadFactor, accessOrder)
        // Setting accessOrder to 'true' enforces LRU ordering instead of insertion ordering.
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest){
                // Automatically evicts the least recently used entry when size exceeds capacity
                return size() > LRUCache.this.capacity;
            }
        };
    }

    public int get(int key){
        return cache.getOrDefault(key, -1);
    }

    public void put(int key, int value){
        cache.put(key, value);
    }

}
