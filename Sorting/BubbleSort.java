import java.util.Arrays;

public class BubbleSort {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given an array of n integers, sort the array in ascending order
         using the Bubble Sort algorithm.

         Example:
         Input:  [64, 25, 12, 22, 11]
         Output: [11, 12, 22, 25, 64]
        */

        int[] arr = {64, 25, 12, 22, 11};

        // Bubble Sort → TC: Best: O(n), Average: O(n^2), Worst: O(n^2), SC: O(1)
        bubbleSort(arr);

        System.out.println(Arrays.toString(arr));
    }

    /*
     Approach: Bubble Sort
     ---------------------

     Idea and Thought Process:

     1. Understand the Problem:
        - We are given an array of integers.
        - The goal is to sort the array in **ascending order**.
        - Bubble Sort works by repeatedly swapping **adjacent elements**
          if they are in the wrong order.
        - With each pass, the largest element "bubbles up" to the end
          of the array.

     2. Algorithm Steps:
        Step-by-step explanation:

        a) Start from the first element and compare it with the next element.
        b) If the current element is greater than the next element, swap them.
        c) Continue comparing and swapping adjacent elements until the end of the array.
        d) After each full pass, the largest element of the unsorted portion
           is placed at its correct position at the end.
        e) Repeat the process for the remaining unsorted portion until the array is sorted.

        Pseudocode:
        -----------------------
        for i = 0 to n-1:
            swapped = false
            for j = 0 to n-i-2:
                if arr[j] > arr[j+1]:
                    swap arr[j] and arr[j+1]
                    swapped = true
            if swapped == false:
                break
        -----------------------

        Key Points:
        - After the first pass, the largest element is at index n-1.
        - After the second pass, the second largest element is at index n-2.
        - The inner loop runs for a shorter range each time.

     3. Why It Works:
        - By comparing adjacent elements, smaller elements move left
          and larger elements move right.
        - Each pass places one element in its final position.
        - The algorithm stops early if no swaps occur in a pass.

     4. Complexity Analysis:
        - Time Complexity:
            Best Case: O(n)
                → When the array is already sorted (no swaps occur).
            Average Case: O(n^2)
            Worst Case: O(n^2)
                → When the array is sorted in reverse order.
        - Space Complexity:
            O(1) → In-place sorting.
        - Number of swaps:
            Can be large compared to Selection Sort.

     5. Key Characteristics:
        - In-place: Yes
        - Stable: Yes (relative order of equal elements is preserved)
        - Simple to understand and implement
        - More swaps compared to Selection Sort
    */
    private static void bubbleSort(int[] arr) {

        for (int i=0; i<arr.length; i++) {
            boolean swapped = false;

            for (int j=0; j < arr.length-i-1; j++) {
                if (arr[j] > arr[j + 1]) {

                    // Swap adjacent elements
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;

                    swapped = true;
                }
            }

            // If no swaps happened, array is already sorted
            if (!swapped) {
                break;
            }
        }
    }
}

/*
    Dry Run Example:

    Original Array: [64, 25, 12, 22, 11]

    Pass 0:
        - Compare adjacent elements from index 0 to 3

        Compare 64 and 25 → swap
        Array now: [25, 64, 12, 22, 11]

        Compare 64 and 12 → swap
        Array now: [25, 12, 64, 22, 11]

        Compare 64 and 22 → swap
        Array now: [25, 12, 22, 64, 11]

        Compare 64 and 11 → swap
        Array now: [25, 12, 22, 11, 64]

        - Largest element (64) is now at its correct position

    Pass 1:
        - Compare adjacent elements from index 0 to 2

        Compare 25 and 12 → swap
        Array now: [12, 25, 22, 11, 64]

        Compare 25 and 22 → swap
        Array now: [12, 22, 25, 11, 64]

        Compare 25 and 11 → swap
        Array now: [12, 22, 11, 25, 64]

        - Second largest element (25) is now at its correct position

    Pass 2:
        - Compare adjacent elements from index 0 to 1

        Compare 12 and 22 → no swap
        Compare 22 and 11 → swap
        Array now: [12, 11, 22, 25, 64]

    Pass 3:
        - Compare adjacent elements from index 0 to 0

        Compare 12 and 11 → swap
        Array now: [11, 12, 22, 25, 64]

    Pass 4:
        - No swaps needed
        - Array is already sorted
        Final Array: [11, 12, 22, 25, 64]
*/

