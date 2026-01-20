public class Searcha2DMatrix {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given a 2D matrix where:
           1. Integers in each row are sorted from left to right.
           2. The first integer of each row is greater than the last integer of the previous row.
         Return true if a given target exists in the matrix, false otherwise.

         Example:
         Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
         Output: false
         Explanation:
         - Target 13 is not present in any row or column of the matrix.
        */

        int[][] matrix = {
                {1,3,5,7},
                {10,11,16,20},
                {23,30,34,60}
        };
        int target = 13;

        // Brute-force: check every element → TC: O(M*N), SC: O(1)
        System.out.println(searchMatrixBrute1(matrix, target));

        // Brute-force row check + linear search → TC: O(M*N) worst, SC: O(1)
        System.out.println(searchMatrixBrute2(matrix, target));

        // Brute-force row check + binary search → TC: O(M*logN), SC: O(1)
        System.out.println(searchMatrixBrute3(matrix, target));

        // Staircase search: start top-right → TC: O(M+N), SC: O(1)
        System.out.println(searchMatrixBetter(matrix, target));

        // Binary search treating matrix as 1D → TC: O(log(M*N)), SC: O(1)
        System.out.println(searchMatrixOptimized(matrix, target));
    }

    /*
     Approach 1: Brute-force
     -----------------------
     Problem:
     - Simply check each element in the matrix to see if it equals the target.

     Intuition / Why Brute-force:
     -----------------------------
     - Easiest and most direct approach.
     - No optimization, just scan every row and column.

     Step-by-Step Thought Process:
     -----------------------------
     1. Loop through each row.
     2. Loop through each column in that row.
     3. If matrix[row][col] == target → return true
     4. If no match found → return false

     Complexity Analysis:
     --------------------
     - Time: O(M*N), where M = rows, N = columns
     - Space: O(1)
    */
    private static boolean searchMatrixBrute1(int[][] matrix, int target){
        if(matrix == null || matrix.length ==0)
            return false;

        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int row=0; row<rows; row++){
            for(int col=0; col<cols; col++){
                if(matrix[row][col] == target){
                    return true;
                }
            }
        }

        return false;
    }

    /*
     Approach 2: Row check + linear search
     --------------------------------------
     Problem:
     - Instead of searching every element, only search in rows where the target may exist.

     Intuition:
     - Each row is sorted → target must be >= first element and <= last element.
     - Skip rows where target cannot exist.

     Complexity Analysis:
     --------------------
     - Time: O(M*N) worst case (if target is in the last row or not present)
     - Space: O(1)
    */
    private static boolean searchMatrixBrute2(int[][] matrix, int target){
        if(matrix == null || matrix.length ==0)
            return false;

        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int row=0; row<rows; row++){

            if(matrix[row][0] <= target && target <= matrix[row][cols-1]){
                for(int col=0; col<cols; col++){
                    if(matrix[row][col] == target){
                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }

    /*
     Approach 3: Row check + Binary Search
     -------------------------------------
     Problem:
     - Each row is sorted → perform binary search in the candidate row.

     Intuition:
     - Reduces search in a row from O(N) → O(logN)
     - Still check each row sequentially.

     Complexity Analysis:
     --------------------
     - Time: O(M * logN)
     - Space: O(1)
    */
    private static boolean searchMatrixBrute3(int[][] matrix, int target){
        if(matrix == null || matrix.length == 0)
            return false;

        int rows = matrix.length;
        int cols = matrix[0].length;

        for(int row=0; row<rows; row++){
            if(matrix[row][0] <= target && target <= matrix[row][cols-1]){
                int low = 0;
                int high = cols-1;

                while(low <= high){
                    int mid = low + (high-low)/2;

                    if(matrix[row][mid] == target)
                        return true;
                    else if(matrix[row][mid] < target)
                        low = mid + 1;
                    else
                        high = mid - 1;
                }

                return false;
            }
        }

        return false;
    }

    /*
     Approach 4: Staircase Search (Top-right)
     ----------------------------------------
     Problem:
     - The matrix has the following properties:
       1. Each row is sorted in ascending order.
       2. Each column is sorted in ascending order.
     - We want to efficiently determine if a target exists in the matrix without scanning every element.

     Intuition:
     - Start from the **top-right corner** of the matrix (row = 0, col = last column).
     - At each step:
       1. If the current element equals the target → we have found the target → return true.
       2. If the current element is greater than the target → move **left** (column--) because all elements below in the same column are even larger.
       3. If the current element is less than the target → move **down** (row++) because all elements to the left in the same row are smaller.
     - Repeat this process until we either find the target or move out of the matrix bounds.

     Why it works:
     - Because the top-right element is **the largest element in its row** and **the smallest element in its column**, we can systematically eliminate a row or a column in each step, reducing search space efficiently.
     - Each step reduces either the number of rows remaining or the number of columns remaining by 1.

     Dry Run Example:
     ----------------
     Matrix:
      [ 1,  3,  5,  7 ]
      [10, 11, 16, 20 ]
      [23, 30, 34, 60 ]
     Target: 13

     Step-by-step:

     1. Start at top-right: (row=0, col=3) → value=7
        - 7 < 13 → move down → row=1, col=3

     2. Current element: (row=1, col=3) → value=20
        - 20 > 13 → move left → row=1, col=2

     3. Current element: (row=1, col=2) → value=16
        - 16 > 13 → move left → row=1, col=1

     4. Current element: (row=1, col=1) → value=11
        - 11 < 13 → move down → row=2, col=1

     5. Current element: (row=2, col=1) → value=30
        - 30 > 13 → move left → row=2, col=0

     6. Current element: (row=2, col=0) → value=23
        - 23 > 13 → move left → row=2, col=-1 → out of bounds → target not found → return false

     Complexity Analysis:
     --------------------
     - Time: O(M + N)
       • Worst case: we move at most M rows down and N columns left.
     - Space: O(1)
       • Only row and col indices are used, no extra memory.
    */
    private static boolean searchMatrixBetter(int[][] matrix, int target){
        if(matrix == null || matrix.length == 0)
            return false;

        int rows = matrix.length;
        int cols = matrix[0].length;

        int row = 0;
        int col = cols-1;

        while(row < rows && col >= 0){
            if(matrix[row][col] == target)
                return true;
            else if(matrix[row][col] > target)
                col--;
            else
                row++;
        }

        return false;
    }

    /*
     Approach 5: Binary Search on Flattened 2D Matrix
     ------------------------------------------------
     Problem:
     - The matrix has M rows and N columns, each row is sorted, and the first integer of each row is greater than the last integer of the previous row.
     - We want to search a target efficiently.

     Intuition:
     - Treat the 2D matrix as a sorted 1D array of size M*N (row-major order):
         1. Flatten conceptually: elements of row 0 first, then row 1, etc.
         2. Apply standard binary search on indices 0 to M*N - 1.
     - To map 1D index to 2D coordinates:
         row = index / cols   → tells which row the element is in
         col = index % cols   → tells the column in that row
     - This allows O(log(M*N)) search without actually flattening the matrix.

     Flattening Example (4x4 Matrix):
     --------------------------------
     Matrix:
      [  1,  2,  3,  4 ]
      [  5,  6,  7,  8 ]
      [  9, 10, 11, 12 ]
      [ 13, 14, 15, 16 ]

     1D Mapping (row-major):
     1D idx:   0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15
     Value:    1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16
     row = idx / 4, col = idx % 4:
     | idx | row | col | matrix[row][col] |
     |-----|-----|-----|-----------------|
     |  0  |  0  |  0  |        1        |
     |  1  |  0  |  1  |        2        |
     |  2  |  0  |  2  |        3        |
     |  3  |  0  |  3  |        4        |
     |  4  |  1  |  0  |        5        |
     |  5  |  1  |  1  |        6        |
     |  6  |  1  |  2  |        7        |
     |  7  |  1  |  3  |        8        |
     |  8  |  2  |  0  |        9        |
     |  9  |  2  |  1  |       10        |
     | 10  |  2  |  2  |       11        |
     | 11  |  2  |  3  |       12        |
     | 12  |  3  |  0  |       13        |
     | 13  |  3  |  1  |       14        |
     | 14  |  3  |  2  |       15        |
     | 15  |  3  |  3  |       16        |

     Dry Run Example:
     ----------------
     Target = 11
     - Matrix has 4 rows, 4 cols → total 16 elements.
     - Initial low=0, high=15
     1. mid = (0+15)/2 = 7 → row=7/4=1, col=7%4=3 → matrix[1][3]=8 < 11 → low = mid+1 = 8
     2. mid = (8+15)/2 = 11 → row=11/4=2, col=11%4=3 → matrix[2][3]=12 > 11 → high = mid-1 = 10
     3. mid = (8+10)/2 = 9 → row=9/4=2, col=9%4=1 → matrix[2][1]=10 < 11 → low=mid+1=10
     4. mid = (10+10)/2 = 10 → row=10/4=2, col=10%4=2 → matrix[2][2]=11 → FOUND

     Key Takeaways:
     ----------------
     - Using row = idx/cols and col = idx%cols lets us navigate the 2D matrix as a 1D array.
     - Binary search ensures O(log(M*N)) time.
     - No extra space is needed (O(1) space).

     Extra Note:
     -----
     - idx = row * cols + col → use this to convert 2D coordinates back to 1D index if needed.
     - This method is only valid if the matrix satisfies:
       1. Each row is sorted.
       2. The first element of each row > last element of previous row.
     - Conceptual flattening avoids extra memory overhead.

     Complexity Analysis:
     --------------------
     - Time: O(log(M*N))
     - Space: O(1)
    */
    private static boolean searchMatrixOptimized(int[][] matrix, int target){
        if(matrix == null || matrix.length ==0)
            return false;

        int rows = matrix.length;
        int cols = matrix[0].length;

        int low = 0;
        int high = rows * cols - 1;

        while(low <= high){
            int mid = low + (high-low)/2;

            int row = mid/cols;
            int col = mid%cols;

            if(matrix[row][col] == target)
                return true;
            else if(matrix[row][col] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return false;
    }

}
