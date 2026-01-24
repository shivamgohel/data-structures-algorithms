// https://leetcode.com/problems/time-based-key-value-store/description/

import java.util.*;


/*

  Problem Statement:
 ------------------
 Design a time-based key-value store that supports:
 1. set(key, value, timestamp)
    - Stores the key with the given value at the given timestamp.
 2. get(key, timestamp)
    - Returns the value associated with the largest timestamp
      less than or equal to the given timestamp.
    - If no such timestamp exists, return an empty string "".

 Example:
 --------
 set("foo", "bar", 1)
 get("foo", 1) → "bar"
 get("foo", 3) → "bar"
 set("foo", "baz", 4)
 get("foo", 4) → "baz"
 get("foo", 5) → "baz"

 Intuition:
 ----------
 Each key can have multiple values over time.
 We store all values along with their timestamps and retrieve
 the most recent valid value for a given timestamp.

 Data Structure Used:
 --------------------
 Map<String, List<Pair>>
 - Key   → String
 - Value → List of (timestamp, value) pairs

 The list of pairs is always sorted by timestamp because
 LeetCode guarantees timestamps are strictly increasing.

 Approach (Brute Force):
 -----------------------
 1. Use a HashMap to group values by key.
 2. For each key, maintain a list of (timestamp, value) pairs.
 3. For get():
    - Traverse the list from the end (latest timestamp).
    - Return the first value whose timestamp ≤ requested timestamp.

 Dry Run:
 --------
 Stored:
 "foo" → [(1,"bar"), (4,"baz")]

 get("foo", 3):
 - Start from (4,"baz") → 4 > 3 → skip
 - Move to (1,"bar") → 1 ≤ 3 → return "bar"

 Complexity Analysis:
 --------------------
 set():
 - Time: O(1)
 - Space: O(1) per insertion

 get():
 - Time: O(n) in worst case
 - Space: O(1)

 Notes:
 ------
 - This is a brute-force solution.
*/
//public class TimeMap {
//
//    static class Pair {
//        int timestamp;
//        String value;
//
//        Pair(int timestamp, String value){
//            this.timestamp = timestamp;
//            this.value = value;
//        }
//    }
//
//    private Map<String, List<Pair>> mp;
//
//    public TimeMap(){
//        mp = new HashMap<>();
//    }
//
//    public void set(String key, String value, int timestamp){
//        mp.putIfAbsent(key, new ArrayList<>());
//        mp.get(key).add(new Pair(timestamp, value));
//    }
//
//    public String get(String key, int timestamp){
//        if(!mp.containsKey(key))
//            return "";
//
//        List<Pair> list = mp.get(key);
//        for(int i= list.size()-1; i>=0; i--){
//            if(list.get(i).timestamp <= timestamp)
//                return list.get(i).value;
//        }
//
//        return "";
//    }
//}



// *** OPTIMIZED APPROACH *** //

/*
 Problem Statement:
 ------------------
 Design a time-based key-value store that supports:
 1. set(key, value, timestamp)
    - Stores the key with the given value at the given timestamp.
 2. get(key, timestamp)
    - Returns the value associated with the largest timestamp
      less than or equal to the given timestamp.
    - If no such timestamp exists, return an empty string "".

 Example:
 --------
 set("foo", "bar", 1)
 get("foo", 1) → "bar"
 get("foo", 3) → "bar"
 set("foo", "baz", 4)
 get("foo", 4) → "baz"
 get("foo", 5) → "baz"

 Intuition:
 ----------
 Each key can have multiple values over time.
 We store all values along with their timestamps and retrieve
 the most recent valid value for a given timestamp.

 Data Structure Used:
 --------------------
 Map<String, List<Pair>>
 - Key   → String
 - Value → List of (timestamp, value) pairs

 The list of pairs is always sorted by timestamp because
 LeetCode guarantees timestamps are strictly increasing.

 Optimized Approach (Binary Search):
 ----------------------------------
 Instead of scanning the list linearly, we use binary search
 to find the largest timestamp that is less than or equal to
 the requested timestamp.

 Key Observations:
 -----------------
 1. Since timestamps are strictly increasing, the list is sorted.
 2. We can apply binary search to efficiently locate the answer.
 3. While searching:
    - If mid.timestamp ≤ given timestamp:
        → This is a valid candidate, store its value
        → Move right to find a larger valid timestamp
    - Else:
        → Move left

 Dry Run:
 --------
 Stored:
 "foo" → [(1,"bar"), (4,"baz")]

 get("foo", 3):
 low=0, high=1
 mid=0 → timestamp=1 ≤ 3 → ans="bar", low=1
 mid=1 → timestamp=4 > 3 → high=0
 Loop ends → return "bar"

 Complexity Analysis:
 --------------------
 set():
 - Time: O(1)
 - Space: O(1) per insertion

 get():
 - Time: O(log n)
 - Space: O(1)

 Notes:
 ------
 - This is the optimized solution.
 - Much faster than brute force for large inputs.
*/
public class TimeMap {

    static class Pair {
        int timestamp;
        String value;

        Pair(int timestamp, String value){
            this.timestamp = timestamp;
            this.value = value;
        }
    }

    private Map<String, List<Pair>> mp;

    public TimeMap(){
        mp = new HashMap<>();
    }

    public void set(String key, String value, int timestamp){
        mp.putIfAbsent(key, new ArrayList<>());
        mp.get(key).add(new Pair(timestamp, value));
    }

    public String get(String key, int timestamp){
        if(!mp.containsKey(key))
            return "";

        List<Pair> list = mp.get(key);

        int low = 0;
        int high = list.size()-1;
        String ans = "";

        while(low <= high){
            int mid = low + (high-low)/2;

            if(list.get(mid).timestamp <= timestamp){
                ans = list.get(mid).value;
                low = mid + 1;
            }else{
                high = mid - 1;
            }
        }

        return ans;
    }

}
