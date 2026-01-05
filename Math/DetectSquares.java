// https://leetcode.com/problems/detect-squares/description/


import java.util.*;


/*
Problem Statement:
Implement DetectSquares class with:
- add(point): adds a point (x, y) to the data structure
- count(point): counts how many squares can be formed with the query point as one corner

Constraints:
- Points are integers >= 0
- Squares are axis-aligned
- Multiple points can exist at the same coordinates

Example:
DetectSquares ds = new DetectSquares();
ds.add([3, 10]);
ds.add([11, 2]);
ds.add([3, 2]);
ds.count([11, 10]); // Output: 1
ds.count([14, 8]);  // Output: 0
ds.add([11, 2]);
ds.count([11, 10]); // Output: 2
*/



/*

/*
Approach 1: Brute Force using List and HashMap
----------------------------------------------

Idea and Thought Process:

1. Understanding the Problem:
   - We need to store points and count how many axis-aligned squares can be formed
     with a given query point as one corner.
   - Brute force means we will check **every existing point** to see if it can form
     a diagonal with the query point and complete a square.
   - Multiple points at the same coordinates are allowed → we must track counts.

2. Data Structures:
   - List<int[]> points → stores all points added (needed to iterate all points)
   - Map<String, Integer> countMap → maps "x,y" to how many times that point was added
     * This helps handle duplicates when calculating squares

3. Algorithm for add(point):
   - Add the point to the list
   - Increment its count in the countMap
   - Complexity:
     * Time: O(1) per addition
     * Space: O(1) per point

4. Algorithm for count(point):
   - Let the query point be (x, y)
   - Initialize result counter = 0
   - Iterate over **all points** in the list
       a) Skip points that share the same x or y → cannot be a diagonal
       b) Check if the difference in x and y coordinates is equal (Math.abs(x1-x) == Math.abs(y1-y))
          → only then a square can be formed (all sides equal)
       c) Identify the other two corners of the square:
          - (x, y1) and (x1, y)
       d) Multiply their counts from countMap and add to the result
          → handles duplicate points automatically
   - Return the total count

5. Complexity Analysis:
   - Time Complexity: O(n) per query, where n = number of points added
     * Each query checks all points for possible diagonals
   - Space Complexity: O(n) for the list and hashmap storing points

6. Advantages:
   - Simple and straightforward to implement
   - Handles duplicates correctly

7. Drawbacks:
   - Slow for large datasets because every query iterates over all points
   - Not suitable for many queries with a large number of points


public class DetectSquares {
    List<int[]> points = new ArrayList<>();
    Map<String, Integer> countMap = new HashMap<>();

    public DetectSquares(){
    }

    public void add(int[] point){
        points.add(point);

        String key = point[0] + "," + point[1];
        countMap.put(key, countMap.getOrDefault(key, 0) +1);
    }

    public int count(int[] point){
        int x = point[0];
        int y = point[1];
        int res = 0;

        for(int[] p : points){
            int x1 = p[0];
            int y1 = p[1];

            // Skip if point is in the same row or column
            // Squares need a diagonal, so x or y should not be equal
            if(x == x1 || y == y1)
                continue;

            // Check if the distance between x-coordinates equals distance between y-coordinates
            // Only then it can form a square (all sides equal)
            if(Math.abs(x1 - x) != Math.abs(y1 - y))
                continue;

            String p1 = x + "," + y1;
            String p2 = x1 + "," + y;

            res += countMap.getOrDefault(p1, 0) * countMap.getOrDefault(p2, 0);
        }

        return res;
    }

}
*/


/*
Approach 2: Optimized using Nested HashMap
------------------------------------------

Idea and Thought Process:

1. Understanding the Problem:
   - We need to store points on a 2D plane and count how many axis-aligned squares
     can be formed with a query point as one corner.
   - Brute force checks all points → inefficient if many points exist.
   - Optimized idea:
       * For a square with query point (x, y):
           - Look at all points in the same column (same x-coordinate)
           - Each point (x, ny) in the same column can form the **vertical side**
             of a potential square
           - Then only need to check points in columns x + side and x - side (horizontal side)
           - Multiply counts of all three other points to handle duplicates

2. Data Structure:
   - Nested HashMap: Map<Integer, Map<Integer, Integer>> points
       * Outer key = x-coordinate
       * Inner key = y-coordinate → value = count of that point
   - Benefits:
       * Fast lookup for any point O(1)
       * Automatically handles duplicates
       * Iterating only over points in same column → much faster than checking all points

3. Algorithm for add(point):
   - If x-coordinate is new, create a new inner map for y-coordinates
   - Increment count of (x, y) in inner map
   - Complexity:
       * Time: O(1) per addition
       * Space: O(1) per point

4. Algorithm for count(point):
   - Let the query point be (x, y)
   - If no points exist in column x → return 0
   - Initialize result = 0
   - Iterate over all points in the same column (x-coordinate):
       a) Let vertical partner = (x, ny)
       b) Compute side length = ny - y
       c) Skip if ny == y (same point)
       d) For each vertical partner, check squares to the right (x + side) and left (x - side)
       e) Multiply counts of:
           - Vertical partner (x, ny)
           - Other two corners ((x ± side, y), (x ± side, ny))
       f) Add the product to result
   - Return result

5. Complexity Analysis:
   - Time Complexity: O(k) per query, where k = number of points in same column
       * Much faster than brute force O(n) if n is large
   - Space Complexity: O(n), where n = number of points added

6. Advantages:
   - Handles duplicates naturally
   - Very efficient for multiple queries
   - No need to iterate over all points

7. Edge Cases:
   - Query point not in any column → return 0
   - Vertical partner = same as query → skip
   - Multiple squares with same query → all counted
*/
public class DetectSquares{
    private Map<Integer, Map<Integer, Integer>> points;

    public DetectSquares(){
        points = new HashMap<>();
    }

    public void add(int[] point){
        int x = point[0];
        int y = point[1];

        points.putIfAbsent(x, new HashMap<>());
        Map<Integer, Integer> yMap = points.get(x);
        yMap.put(y, yMap.getOrDefault(y, 0) +1);
    }

    public int count(int[] point){
        int x = point[0];
        int y = point[1];

        if(!points.containsKey(x))
            return 0;

        int res = 0;
        Map<Integer, Integer> yMap = points.get(x);

        for(int ny : yMap.keySet()){
            int side = ny - y;
            int countVertical = yMap.get(ny);

            res += countVertical * getCount(x+side, y) * getCount(x+side, ny);
            res += countVertical * getCount(x-side, y) * getCount(x-side, ny);
        }

        return res;
    }

    private int getCount(int x, int y){
        if(!points.containsKey(x))
            return 0;

        return points.get(x).getOrDefault(y, 0);
    }
}



