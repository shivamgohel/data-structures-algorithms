// https://leetcode.com/problems/set-matrix-zeroes/description/

public class SetMatrixZeroes {
    public static void main(String[] args) {

        /*
         Problem Statement:
         You are given an m x n matrix.
         If an element is 0, set its entire row and column to 0.
         Do it in-place.

         Example:
         Input:
         1 1 1
         1 0 1
         1 1 1

         Output:
         1 0 1
         0 0 0
         1 0 1
        */

        int[][] matrix = {{1,1,1},
                          {1,0,1},
                          {1,1,1}};

        // Brute-force approach → TC: O((m×n)×(m+n)), SC: O(1)
        setZeroesBrute(matrix);

        // Better approach (extra space) → TC: O(m×n), SC: O(m+n)
        setZeroesBetter(matrix);

        // Optimized in-place approach → TC: O(m×n), SC: O(1)
        setZeroesOptimzied(matrix);
    }

    /*
     ------------------------------------------------
     Approach 1: Brute Force (Using Marker Value)
     ------------------------------------------------

     Idea and Thought Process:

     1. Understand the problem:
        - When we find a zero at (row, col),
          the entire row and column must become zero.
        - But we cannot immediately set them to zero,
          because it may affect future checks.

     2. Strategy:
        - When we encounter a zero, we "mark" its row and column
          using a special value (here -1).
        - Later, convert all marked cells (-1) into 0.

     3. Important Note:
        - This approach is NOT safe for all inputs because
          the matrix may already contain -1.
        - Used only for learning brute-force logic.

     4. Steps:
        - Traverse the matrix.
        - On encountering 0:
            → mark entire row
            → mark entire column
        - In a second pass, convert all -1 to 0.

     5. Complexity:
        - Time: O((m × n) × (m + n))  (worst case)
        - Space: O(1)
    */
    private static void setZeroesBrute(int[][] matrix){
        int rowSize = matrix.length;
        int colSize = matrix[0].length;

        for(int row=0; row<rowSize; row++){
            for(int col=0; col<colSize; col++){
                if(matrix[row][col] == 0){
                    markRow(row, matrix);
                    markCol(col, matrix);
                }
            }
        }

        for(int row=0; row<rowSize; row++){
            for(int col=0; col<colSize; col++){
                if(matrix[row][col] == -1){
                    matrix[row][col] = 0;
                }
            }
        }
    }
    private static void markRow(int row, int[][] matrix){
        for(int col=0; col<matrix[0].length; col++){
            if(matrix[row][col] != 0){
                matrix[row][col] = -1;
            }
        }
    }
    private static void markCol(int col, int[][] matrix){
        for(int row=0; row<matrix.length; row++){
            if(matrix[row][col] != 0){
                matrix[row][col] = -1;
            }
        }
    }

    /*
     ------------------------------------------------
     Approach 2: Better Solution (Using Extra Arrays)
     ------------------------------------------------

     Idea and Thought Process:

     1. Problem with brute-force:
        - Using a marker value is unsafe.
        - Time complexity is inefficient.

     2. Improved strategy:
        - Use two boolean arrays:
            zeroRow[] → tracks rows that must be zero
            zeroCol[] → tracks columns that must be zero

     3. Steps:
        - First pass:
            If matrix[row][col] == 0
            → zeroRow[row] = true
            → zeroCol[col] = true
        - Second pass:
            If zeroRow[row] OR zeroCol[col] is true
            → set matrix[row][col] = 0

     4. Complexity:
        - Time: O(m × n)
        - Space: O(m + n)
    */
    private static void setZeroesBetter(int[][] matrix){
        int rowSize = matrix.length;
        int colSize = matrix[0].length;

        boolean[] zeroRow = new boolean[rowSize];
        boolean[] zeroCol = new boolean[colSize];

        for(int row=0; row<rowSize; row++){
            for(int col=0; col<colSize; col++){
                if(matrix[row][col] == 0){
                    zeroRow[row] = true;
                    zeroCol[col] = true;
                }
            }
        }

        for(int row=0; row<rowSize; row++){
            for(int col=0; col<colSize; col++){
                if(zeroRow[row] || zeroCol[col]){
                    matrix[row][col] = 0;
                }
            }
        }
    }

    /*
     ------------------------------------------------
     Approach 3: Optimized In-Place (O(1) Space)
     ------------------------------------------------

     Idea and Thought Process:

     1. Goal:
        - Solve the problem without using extra space.

     2. Key Observation:
        - We can use the FIRST ROW and FIRST COLUMN
          as marker arrays.

     3. Challenge:
        - First row and first column may themselves
          need to be zeroed.
        - Use two boolean flags:
            firstRowZero
            firstColZero

     4. Steps:
        - Step 1: Check if first row contains zero
        - Step 2: Check if first column contains zero
        - Step 3: Use first row & column as markers
        - Step 4: Set inner matrix to zero using markers
        - Step 5: Zero first row if needed
        - Step 6: Zero first column if needed

     5. Complexity:
        - Time: O(m × n)
        - Space: O(1)
    */
    private static void setZeroesOptimzied(int[][] matrix){
        int rowSize = matrix.length;
        int colSize = matrix[0].length;

        boolean firstRowZero = false;
        boolean firstColZero = false;

        for(int col=0; col<colSize; col++){
            if(matrix[0][col] == 0){
                firstRowZero = true;
                break;
            }
        }
        for(int row=0; row<rowSize; row++){
            if(matrix[row][0] == 0){
                firstColZero = true;
                break;
            }
        }

        for(int row=1; row<rowSize; row++){
            for(int col=1; col<colSize; col++){
                if(matrix[row][col] == 0){
                    matrix[0][col] = 0;
                    matrix[row][0] = 0;
                }
            }
        }

        for(int row=1; row<rowSize; row++){
            for(int col=1; col<colSize; col++){
                if(matrix[0][col] == 0||  matrix[row][0] == 0){
                    matrix[row][col] = 0;
                }
            }
        }

        if(firstRowZero){
            for(int col=0; col<colSize; col++){
                matrix[0][col] = 0;
            }
        }
        if(firstColZero){
            for(int row=0; row<rowSize; row++){
                matrix[row][0] = 0;
            }
        }

    }

}
