import java.util.Arrays;

public class InsertionSort {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Given an array of n integers, sort the array in ascending order
         using the Insertion Sort algorithm with swapping.

         Example:
         Input:  [64, 25, 12, 22, 11]
         Output: [11, 12, 22, 25, 64]
        */

        int[] arr = {64, 25, 12, 22, 11};

        // Insertion Sort (swap-based) → TC: Best: O(n), Average: O(n^2), Worst: O(n^2), SC: O(1)
        insertionSortSwap(arr);

        System.out.println(Arrays.toString(arr));
    }

    /*
     Approach: Insertion Sort (Swap-based)
     --------------------------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - We are given an array of integers.
        - The goal is to sort the array in ascending order.
        - Instead of shifting, we **swap the key element with its previous element**
          until it reaches its correct position in the sorted portion.

     2. Algorithm Steps:
        Step-by-step explanation:

        a) Start from the second element (index 1).
        b) Compare it with the previous element.
        c) If the previous element is greater, swap them.
        d) Continue swapping backwards until the element is at the correct position.
        e) Repeat for all elements.

        Pseudocode:
        -----------------------
        for i = 1 to n-1:
            j = i
            while j > 0 and arr[j] < arr[j-1]:
                swap arr[j] and arr[j-1]
                j = j - 1
        -----------------------

        Key Points:
        - Each pass "bubbles" the current element to its correct place in the sorted portion.
        - Simpler to visualize than shifting.
        - Works well for small or nearly sorted arrays.

     3. Why It Works:
        - By swapping backwards, we ensure that the left portion remains sorted.
        - Eventually, all elements are in ascending order.

     4. Complexity Analysis:
        - Time Complexity:
            Best Case: O(n)
                → Array already sorted (no swaps occur)
            Average Case: O(n^2)
            Worst Case: O(n^2)
                → Array sorted in reverse
        - Space Complexity:
            O(1) → In-place sorting
        - Stable: Yes (relative order of equal elements is preserved)

     5. Key Characteristics:
        - In-place: Yes
        - Stable: Yes
        - Simple to implement
        - Easier to dry run with swaps than shifts
    */
    private static void insertionSortSwap(int[] arr) {

        for (int i=1; i<arr.length; i++) {
            int j = i;

            // Swap backwards until element is in correct position
            while (j>0 && arr[j]<arr[j-1]) {
                int temp = arr[j];
                arr[j] = arr[j-1];
                arr[j-1] = temp;

                j--;
            }
        }
    }
}

/*
    Dry Run Example (Swap-based):

    Original Array: [64, 25, 12, 22, 11]

    Step 1: i = 1, element = 25
        Compare 25 and 64 → 25 < 64 → swap
        Array now: [25, 64, 12, 22, 11]

    Step 2: i = 2, element = 12
        Compare 12 and 64 → swap → [25, 12, 64, 22, 11]
        Compare 12 and 25 → swap → [12, 25, 64, 22, 11]

    Step 3: i = 3, element = 22
        Compare 22 and 64 → swap → [12, 25, 22, 64, 11]
        Compare 22 and 25 → swap → [12, 22, 25, 64, 11]

    Step 4: i = 4, element = 11
        Compare 11 and 64 → swap → [12, 22, 25, 11, 64]
        Compare 11 and 25 → swap → [12, 22, 11, 25, 64]
        Compare 11 and 22 → swap → [12, 11, 22, 25, 64]
        Compare 11 and 12 → swap → [11, 12, 22, 25, 64]

    Final Array: [11, 12, 22, 25, 64]
*/
