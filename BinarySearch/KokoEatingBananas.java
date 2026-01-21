// https://leetcode.com/problems/koko-eating-bananas/description/

import java.util.Arrays;

public class KokoEatingBananas {
    public static void main(String[] args) {

        /*
         Problem Statement:
         -----------------
         Koko loves to eat bananas. There are n piles of bananas, the i-th pile has piles[i] bananas.
         The guards have gone and will come back in h hours.

         Koko can decide her eating speed k (bananas/hour). Each hour, she chooses some pile and eats k bananas
         from that pile. If the pile has less than k bananas, she eats all of them instead, and won't eat any more bananas during that hour.

         Return the **minimum integer k** such that she can eat all the bananas within h hours.

         Example:
         --------
         Input: piles = [3,6,7,11], h = 8
         Output: 4
         Explanation:
         - Eating speed 4 bananas/hour allows Koko to finish in 8 hours.
         - 1st pile: 3 → 1 hour
         - 2nd pile: 6 → 2 hours
         - 3rd pile: 7 → 2 hours
         - 4th pile: 11 → 3 hours
         - Total = 8 hours
        */

        int[] piles = {3,6,7,11};
        int h = 8;

        // Approach 1: Brute-force → check all possible speeds → TC: O(n * maxPile), SC: O(1)
        System.out.println(minEatingSpeedBrute(piles, h));

        // Approach 2: Binary Search → search for minimum valid speed → TC: O(n * log(maxPile)), SC: O(1)
        System.out.println(minEatingSpeedOptimized(piles, h));

    }

    /*
     Approach 1: Brute-force
     -----------------------
     Problem:
     - Koko has piles of bananas.
     - She can choose an eating speed k (bananas/hour).
     - We want the **minimum integer k** such that she can eat all bananas within h hours.

     Intuition / Why Brute-force:
     -----------------------------
     - The simplest approach is to try **every possible speed k** from 1 to the maximum pile size.
     - For each speed k, calculate the total hours required to finish all piles.
     - If the total hours ≤ h, k is a valid speed.
     - The **first valid k** is the answer because we are increasing k from 1 upwards.

     Step-by-Step Thought Process:
     -----------------------------
     1. Find the maximum pile size:
        - The fastest Koko would ever need to eat is equal to the largest pile.
        - So maxPile = max(piles)
     2. Loop through all possible speeds k = 1 → maxPile
        - For each k, calculate total hours:
            - For each pile:
                - Hours to eat this pile = ceil(pile / k)
                  → implemented as (pile + k - 1)/k
        - Sum the hours for all piles
     3. If total hours ≤ h:
        - Return current k as the answer
     4. If no k satisfies the condition → return -1 (edge case, usually unnecessary)

     -----------------------------
     How ceil(pile/k) works using (pile + k - 1)/k
     ------------------------------------------------
     - In integer division in Java, 7/3 = 2 (floor division)
     - But we need **ceiling division**: ceil(7/3) = 3

     Trick formula:
       ceil(pile/k) = (pile + k - 1) / k

     Why it works:
       - If pile is divisible by k, e.g., 6/3:
         (6 + 3 - 1)/3 = 8/3 = 2 → same as 6/3
       - If pile is not divisible by k, e.g., 7/3:
         (7 + 3 - 1)/3 = 9/3 = 3 → ceil division

     Examples:
     1. pile = 7, k = 3
        ceil(7/3) = 3
        (7 + 3 - 1)/3 = 9/3 = 3
     2. pile = 10, k = 4
        ceil(10/4) = 3
        (10 + 4 - 1)/4 = 13/4 = 3
     3. pile = 8, k = 4
        ceil(8/4) = 2
        (8 + 4 - 1)/4 = 11/4 = 2

     This avoids using floating-point division or Math.ceil, keeping everything as integers.

     Complexity Analysis:
     --------------------
     - Time: O(n * maxPile), n = number of piles
       • For each k from 1 to maxPile, sum over all piles
     - Space: O(1)
    */
    private static int minEatingSpeedBrute(int[] piles, int h){
        int maxPiles = 0;
        for(int pile : piles)
            maxPiles = Math.max(maxPiles, pile);
        // int maxPiles = Arrays.stream(piles).max().getAsInt();  { shortcut way }

        for(int k=1; k<=maxPiles; k++){
            long hours = 0;
            for(int pile : piles){
                hours += (pile + k - 1)/k;   // 1 + (pile-1)/k    { another way  }
            }

            if(hours <= h)
                return k;
        }

        return -1;
    }

    /*
     Approach 2: Binary Search (Optimized)
     -------------------------------------
     Problem:
     - Koko has piles of bananas and h hours to finish them.
     - We want the **minimum eating speed k** such that she can finish all bananas in h hours.

     Intuition:
     -----------
     1. **Monotonic behavior**:
        - Total hours needed decreases as eating speed k increases.
        - If k is too slow → hours > h → we need a faster speed
        - If k is fast enough → hours ≤ h → maybe we can go slower
        - This is why **binary search works** on the speed k.

     2. **Range of k**:
        - Minimum speed = 1 (eat at least 1 banana/hour)
        - Maximum speed = max pile size (no need to eat faster than largest pile)

     Step-by-Step Thought Process:
     -----------------------------
     1. Find the maximum pile size: maxPile = Arrays.stream(piles).max().getAsInt();
        - This is the upper bound for speed k
     2. Initialize binary search:
        - low = 1
        - high = maxPile
        - ans = maxPile (default answer)
     3. While low <= high:
        a. mid = low + (high - low)/2 → candidate speed k
        b. Compute total hours needed at speed mid:
           - hours = sum over piles of ceil(pile / mid)
           - Using integer trick: ceil(pile / mid) = (pile + mid - 1) / mid
        c. Check if this speed is sufficient:
           - If hours <= h → valid speed:
             • Store as potential answer: ans = mid
             • Try slower speed to minimize k → high = mid - 1
           - Else → too slow → increase speed → low = mid + 1
     4. Return ans (minimum valid speed)

     How ceil(pile / mid) works (integer trick):
     --------------------------------------------
     - Integer division in Java floors the result: 7/3 = 2
     - We need ceiling division: ceil(7/3) = 3
     - Formula: (pile + mid - 1) / mid

     Examples:
     - pile = 7, mid = 3 → (7 + 3 - 1)/3 = 9/3 = 3
     - pile = 6, mid = 3 → (6 + 3 - 1)/3 = 8/3 = 2
     - pile = 1, mid = 3 → (1 + 3 - 1)/3 = 3/3 = 1

     Dry Run Example:
     ----------------
     piles = [3,6,7,11], h = 8
     maxPile = 11
     low = 1, high = 11, ans = 11

     1. mid = (1+11)/2 = 6
        - hours = ceil(3/6)+ceil(6/6)+ceil(7/6)+ceil(11/6)
                 = 1 + 1 + 2 + 2 = 6 ≤ 8 → valid
        - ans = 6, try slower → high = mid - 1 = 5

     2. mid = (1+5)/2 = 3
        - hours = ceil(3/3)+ceil(6/3)+ceil(7/3)+ceil(11/3)
                 = 1 + 2 + 3 + 4 = 10 > 8 → too slow
        - low = mid + 1 = 4

     3. mid = (4+5)/2 = 4
        - hours = ceil(3/4)+ceil(6/4)+ceil(7/4)+ceil(11/4)
                 = 1 + 2 + 2 + 3 = 8 ≤ 8 → valid
        - ans = 4, try slower → high = mid - 1 = 3

     4. low > high → exit loop
        - Minimum speed = 4

     Why it works:
     --------------
     - Monotonic decreasing hours with increasing speed allows binary search.
     - At each step, we **eliminate half of the search space**.
     - Integer ceil trick avoids floating-point operations.

     Complexity Analysis:
     --------------------
     - Time: O(n * log(maxPile))
       • n = number of piles
       • log(maxPile) = number of binary search iterations
     - Space: O(1) → only variables used, no extra data structures
    */
    private static int minEatingSpeedOptimized(int[] piles, int h){
        int maxPiles = Arrays.stream(piles).max().getAsInt();

        int low = 1;
        int high = maxPiles;
        int ans = maxPiles;

        while(low <= high){
            int mid = low + (high-low)/2;  // mid is our k here

            long hours = 0;
            for(int pile : piles){
                hours += (pile + mid - 1)/mid;
            }

            if(hours <= h){
                ans = mid;
                high = mid - 1;
            }else{
                low = mid + 1;
            }
        }

        return ans;
    }

}
