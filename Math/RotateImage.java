// https://leetcode.com/problems/rotate-image/description/

public class RotateImage {
    public static void main(String[] args) {

        /*
         Problem Statement:
         You are given an n x n 2D matrix representing an image,
         rotate the image by 90 degrees (clockwise) in-place.

         Example 1:
         Input: matrix = [[1,2,3],
                          [4,5,6],
                          [7,8,9]]

         Output:         [[7,4,1],
                          [8,5,2],
                          [9,6,3]]
        */


        int[][] matrix = {
                {1,2,3},
                {4,5,6},
                {7,8,9}
        };

        // Brute-force approach using extra space
        rotateImageBrute(matrix);

        // Optimized in-place approach
        rotateImageOptimized(matrix);

    }

    /*
     Approach 1: Brute-force using extra matrix
     --------------------------------------------
     Idea and Thought Process:

     1. Understand the Problem:
        - We are asked to rotate an n x n matrix by 90 degrees clockwise.
        - Clockwise rotation means:
            - The first row becomes the last column
            - The second row becomes the second-last column, and so on.

        Example:
            Original Matrix:
            1 2 3
            4 5 6
            7 8 9

            Rotated Matrix (90° clockwise):
            7 4 1
            8 5 2
            9 6 3

     2. Observe the Pattern:
        - Look at the indices carefully.
        - Element at position (row, col) in the original matrix moves to:
            (col, n - 1 - row) in the rotated matrix
            Explanation:
            - `col` becomes the new row because columns shift downward.
            - `n-1-row` becomes the new column because the first row moves to last column.

        Example:
            - Original (0,0) → Rotated (0,2)
            - Original (0,1) → Rotated (1,2)
            - Original (1,0) → Rotated (0,1)
            This matches the rotated matrix perfectly.

     3. Implement Using Extra Matrix:
        - Create a new matrix of the same size to store rotated values.
        - Loop through every element (nested for loops) and assign:
            rotated[col][n-1-row] = matrix[row][col]

     4. Copy Back:
        - Since the problem may require rotation in-place, after filling the extra matrix,
          copy all elements back to the original matrix.

     5. Complexity Analysis:
        - Time Complexity: O(n^2) → we visit every element once.
        - Space Complexity: O(n^2) → we use an extra matrix of the same size.
    */

    public static void rotateImageBrute(int[][] matrix){
        int size = matrix.length;
        int[][] rotatedMatrix = new int[size][size];

        for(int row=0; row<size; row++){
            for(int col=0; col<size; col++){
                rotatedMatrix[col][size-1-row] = matrix[row][col];
            }
        }

        for(int row=0; row<size; row++){
            for(int col=0; col<size; col++){
                matrix[row][col] = rotatedMatrix[row][col];
            }
        }
    }

    /*
     Approach 2: Optimized In-place Rotation
     ---------------------------------------
     Idea and Thought Process:

     1. Goal:
        - Rotate the n x n matrix 90 degrees clockwise **without using extra space**.

     2. Observation:
        - A 90-degree clockwise rotation can be achieved in **two steps**:
            Step 1: Transpose the matrix
            Step 2: Reverse each row

     3. Step 1: Transpose the matrix
        - Transposing means swapping matrix[row][col] with matrix[col][row].
        - This flips the matrix along its main diagonal (top-left to bottom-right).
        - Example:
            Original Matrix:
            1 2 3
            4 5 6
            7 8 9

            After Transpose:
            1 4 7
            2 5 8
            3 6 9

        - Observation: After transpose, **columns become rows**. This is the first part of the rotation.

     4. Step 2: Reverse each row
        - After transpose, we need to make it a clockwise rotation.
        - Reversing each row shifts elements to their final rotated positions.
        - Continuing the example:
            Transposed Matrix:
            1 4 7
            2 5 8
            3 6 9

            Reverse each row:
            7 4 1
            8 5 2
            9 6 3

        - This matches the expected 90-degree rotated matrix.

     5. Why it Works:
        - Transpose + row reversal is equivalent to rotating the matrix clockwise.
        - Intuition: Transpose swaps rows and columns, row reversal completes the clockwise shift.
        - This method avoids extra space because all swaps are done in-place.

     6. Implementation Details:
        - Use a nested loop to transpose only the upper triangle to avoid swapping elements twice.
          (loop `col` from `row` to `n-1`).
        - Use two pointers (`left` and `right`) to reverse each row in-place.

     7. Complexity Analysis:
        - Time Complexity: O(n^2) → every element is visited constant times.
        - Space Complexity: O(1) → no extra matrix is used.

     8. Example Walkthrough:
        - Original:
            1 2 3
            4 5 6
            7 8 9
        - After Transpose:
            1 4 7
            2 5 8
            3 6 9
        - After Reversing Rows:
            7 4 1
            8 5 2
            9 6 3
    */

    public static void rotateImageOptimized(int[][] matrix){
        int size = matrix.length;

        for(int row=0; row<size; row++){
            for(int col=row; col<size; col++){
                int temp = matrix[row][col];
                matrix[row][col] = matrix[col][row];
                matrix[col][row] = temp;
            }
        }

        for(int row=0; row<size; row++){
            int left = 0;
            int right = size-1;

            while(left < right){
                int temp = matrix[row][left];
                matrix[row][left] = matrix[row][right];
                matrix[row][right] = temp;

                left++;
                right--;
            }
        }
    }
}

/*
    WITH UTILITY FUNCTIONS

    private static void transpose(int[][] matrix){
        int size = matrix.length;

        for(int row=0; row<size; row++){
            for(int col=row; col<size; col++){
                swap(matrix,row,col,col,row);
            }
        }
    }

    private static void reverseRows(int[][] matrix){
        int size = matrix.length;

        for(int row=0; row<size; row++){
            int left = 0;
            int right = size-1;

            while(left < right){
                swap(matrix,row,left,row,right);

                left++;
                right--;
            }
        }
    }

    private static void swap(int[][] matrix, int row1, int col1, int row2, int col2){
        int temp = matrix[row1][col1];
        matrix[row1][col1] = matrix[row2][col2];
        matrix[row2][col2] = temp;

    }


 */