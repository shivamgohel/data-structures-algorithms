import java.util.Arrays;

public class SelectionSort {
    public static void main(String[] args) {

        /*
         Problem Statement:
         Given an array of n integers, sort the array in ascending order
         using the Selection Sort algorithm.

         Example:
         Input:  [64, 25, 12, 22, 11]
         Output: [11, 12, 22, 25, 64]
        */

        int[] arr = {64, 25, 12, 22, 11};

        // Selection Sort → TC: Best: O(n^2), Average: O(n^2), Worst: O(n^2), SC: O(1)
        selectionSort(arr);

        System.out.println(Arrays.toString(arr));
    }

    /*
    Approach: Selection Sort
    ------------------------

    Idea and Thought Process:

    1. Understand the Problem:
       - We are given an array of integers.
       - The goal is to sort the array in **ascending order**.
       - Selection Sort works by repeatedly selecting the **smallest element**
         from the unsorted portion and moving it to its correct position in the array.
       - It is an **in-place sorting algorithm**, meaning it does not require extra memory.

    2. Algorithm Steps:
       Step-by-step explanation:

       a) Start with the first element (index i = 0) and assume it is the minimum.
       b) Compare this element with all other elements to its right (unsorted portion).
       c) If a smaller element is found, update the index of the minimum element.
       d) After checking the entire unsorted portion, swap the minimum element found
          with the element at index i.
       e) Move to the next element (i = i + 1) and repeat steps a–d until the array is sorted.

       Pseudocode:
       -----------------------
       for i = 0 to n-1:
           minIndex = i
           for j = i+1 to n-1:
               if arr[j] < arr[minIndex]:
                   minIndex = j
           swap arr[i] and arr[minIndex]
       -----------------------

       Key points:
       - After each iteration, the smallest remaining element is placed in its correct position.
       - The first iteration places the smallest element at index 0, the second iteration places
         the second smallest element at index 1, and so on.

    3. Why It Works:
       - The algorithm ensures that after each pass, the next smallest element is placed correctly.
       - No element in the sorted portion is ever moved again.
       - This continues until all elements are sorted.

    4. Complexity Analysis:
       - Time Complexity:
           Best Case: O(n^2) → Even if the array is already sorted, it still scans the unsorted portion.
           Average Case: O(n^2) → On average, it performs n*(n-1)/2 comparisons.
           Worst Case: O(n^2) → If the array is sorted in descending order, same number of comparisons.
       - Space Complexity:
           O(1) → Only a constant amount of extra memory is used for swapping.
       - Number of swaps: Maximum n-1 swaps, because only one swap is done per iteration.

    5. Key Characteristics:
       - In-place: Yes
       - Stable: No (because swapping may change the relative order of equal elements)
       - Simple to implement and understand
       - Not efficient for large datasets due to O(n^2) complexity
    */
    private static void selectionSort(int[] arr) {

        for (int i=0; i<arr.length; i++) {
            int minIndex = i;
            for (int j=i+1; j<arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}

/*
    Dry Run Example:

    Original Array: [64, 25, 12, 22, 11]

    Iteration 0:
        - Find min in [64, 25, 12, 22, 11] → min = 11
        - Swap 11 with 64
        Array now: [11, 25, 12, 22, 64]

    Iteration 1:
        - Find min in [25, 12, 22, 64] → min = 12
        - Swap 12 with 25
        Array now: [11, 12, 25, 22, 64]

    Iteration 2:
        - Find min in [25, 22, 64] → min = 22
        - Swap 22 with 25
        Array now: [11, 12, 22, 25, 64]

    Iteration 3:
        - Find min in [25, 64] → min = 25
        - Swap 25 with 25 (no change)
        Array now: [11, 12, 22, 25, 64]

    Iteration 4:
        - Last element, already sorted
        Final Array: [11, 12, 22, 25, 64]
*/