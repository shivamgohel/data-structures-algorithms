// https://leetcode.com/problems/spiral-matrix/description/

import java.util.*;

public class SpiralMatrix {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given an m x n matrix, return all elements of the matrix
         in spiral order.

         Example:
         Input:
         matrix = [[1,2,3],
                   [4,5,6],
                   [7,8,9]]

         Output:
         [1, 2, 3, 6, 9, 8, 7, 4, 5]
        */

        int[][] matrix = {
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };

        // Optimized approach using boundary tracking
        spiralOrderOptimized(matrix);

    }

    /*
     Approach: Optimized Spiral Traversal using Boundary Tracking
     ------------------------------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - We are given an m x n matrix.
        - We must return all elements in *spiral order*:
            → left to right
            ↓ top to bottom
            ← right to left
            ↑ bottom to top
        - We continue this process until all elements are visited.

        Example:
            Input Matrix:
            1 2 3
            4 5 6
            7 8 9

            Spiral Order:
            1 → 2 → 3 → 6 → 9 → 8 → 7 → 4 → 5

     2. Key Observation:
        - Spiral traversal happens in **layers**.
        - Each layer can be defined using four boundaries:
            - top    → first unvisited row
            - bottom → last unvisited row
            - left   → first unvisited column
            - right  → last unvisited column
        - After traversing one side, we shrink the corresponding boundary.

     3. Initialize Boundaries:
        - left = 0
        - right = number of columns - 1
        - top = 0
        - bottom = number of rows - 1

     4. Traversal Steps (Repeated While Boundaries Are Valid):

        a) Traverse Left → Right (Top Row):
           - Move across the top boundary from left to right.
           - Add all elements in the top row.
           - Increment `top` because this row is now visited.

        b) Traverse Top → Bottom (Right Column):
           - Move down the right boundary.
           - Add all elements in the right column.
           - Decrement `right` because this column is now visited.

        c) Traverse Right → Left (Bottom Row):
           - Only if `top <= bottom` (to avoid duplicates).
           - Move across the bottom boundary from right to left.
           - Add all elements in the bottom row.
           - Decrement `bottom`.

        d) Traverse Bottom → Top (Left Column):
           - Only if `left <= right`.
           - Move up the left boundary.
           - Add all elements in the left column.
           - Increment `left`.

     5. Why Boundary Checks Are Important:
        - Prevent re-visiting elements.
        - Handle edge cases like:
            - Single row matrix (1 x n)
            - Single column matrix (m x 1)
            - Odd-sized matrices

     6. Loop Termination:
        - The loop continues as long as:
            left <= right AND top <= bottom
        - Once boundaries cross, all elements are visited.

     7. Example Walkthrough:

        Initial:
            left=0, right=2, top=0, bottom=2

        Step 1 (Top row):
            1 2 3
            top++

        Step 2 (Right column):
            6 9
            right--

        Step 3 (Bottom row):
            8 7
            bottom--

        Step 4 (Left column):
            4
            left++

        Remaining:
            5 (center element)

        Final Output:
            [1, 2, 3, 6, 9, 8, 7, 4, 5]

     8. Complexity Analysis:
        - Time Complexity: O(m × n)
          → Every element is visited exactly once.
        - Space Complexity: O(1) extra space
          → No additional data structures used (excluding output list).
    */

    public static List<Integer> spiralOrderOptimized(int[][] matrix){
        List<Integer> res = new ArrayList<>();
        int left = 0;
        int right = matrix[0].length-1;
        int top = 0;
        int bottom = matrix.length-1;

        while(left <= right && top <= bottom){

            // left to right
            for(int col=left; col<=right; col++){
                res.add(matrix[top][col]);
            }
            top++;

            // top to bottom
            for(int row=top; row<=bottom; row++){
                res.add(matrix[row][right]);
            }
            right--;

            // right to left
            if(top <= bottom) {
                for (int col = right; col >= left; col--) {
                    res.add(matrix[bottom][col]);
                }
                bottom--;
            }

            // bottom to top
            if(left <= right) {
                for (int row = bottom; row >= top; row--) {
                    res.add(matrix[row][left]);
                }
                left++;
            }
        }

        return res;
    }

}
