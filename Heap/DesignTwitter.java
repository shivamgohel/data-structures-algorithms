// https://leetcode.com/problems/design-twitter/description/

//HashMaps -> mapping userID to their tweetID and follow relationships
//HashSets -> Tracking followee relationships with O(1) add/remove/lookup
//Heap     -> Merging k sorted lists efficiently to find top-k elements
//Sorting  -> custom comparators for ordering tweets by timestamps

import javax.print.attribute.HashPrintServiceAttributeSet;
import java.util.*;


// BRUTE-FORCE:


/*
 * Problem Statement:
 * -----------------
 * Design a simplified Twitter system with the following operations:
 *
 * 1. postTweet(userId, tweetId)
 *    -> User posts a tweet.
 *
 * 2. getNewsFeed(userId)
 *    -> Return the 10 most recent tweets in the user's news feed.
 *       Feed should include:
 *       - User's own tweets
 *       - Tweets from users they follow
 *       - Sorted by most recent (latest timestamp first)
 *
 * 3. follow(followerId, followeeId)
 *    -> follower follows followee
 *
 * 4. unfollow(followerId, followeeId)
 *    -> follower unfollows followee
 *
 * Constraints:
 * - Return at most 10 tweets
 * - Maintain correct chronological order
 */

/*
 * Data Structures:
 * ----------------
 * 1. followMap: follower -> set of followees
 *    - Helps track relationships efficiently
 *
 * 2. tweetMap: user -> list of tweets
 *    - Each tweet stored as: [timestamp, tweetId]
 *
 * 3. Global time:
 *    - Ensures ordering of tweets (monotonically increasing)
 */
//public class DesignTwitter {
//    private int time;
//    private Map<Integer, Set<Integer>> followMap;
//    private Map<Integer, List<int[]>> tweetMap;
//
//    public DesignTwitter(){
//        time = 0;
//        followMap = new HashMap<>();
//        tweetMap = new HashMap<>();
//    }
//
//    /*
//     * postTweet(userId, tweetId)
//     * --------------------------
//     * Intuition:
//     * - Store tweets per user with timestamp to maintain order.
//     *
//     * Steps:
//     * 1. Initialize list if user not present
//     * 2. Add tweet as [time, tweetId]
//     * 3. Increment global time
//     *
//     * Example:
//     * user 1 posts tweet 5 at time 0 -> [0,5]
//     *
//     * Complexity:
//     * - Time: O(1)
//     * - Space: O(1)
//     */
//    public void postTweet(int userId, int tweetId){
//        tweetMap.putIfAbsent(userId, new ArrayList<>());
//        tweetMap.get(userId).add(new int[]{time++, tweetId});
//    }
//
//
//    /*
//     * getNewsFeed(userId)
//     * -------------------
//     * Intuition:
//     * - Collect all relevant tweets:
//     *   (1) user's own tweets
//     *   (2) followees' tweets
//     *
//     * - Combine → Sort → Take top 10
//     *
//     * Step-by-Step:
//     * -------------
//     * 1. Get user's tweets
//     * 2. For each followee:
//     *      add their tweets
//     * 3. Sort all tweets by timestamp (descending)
//     * 4. Extract first 10 tweetIds
//     *
//     * Dry Run:
//     * --------
//     * user 1 follows user 2
//     *
//     * user 1 tweets: [t1, A]
//     * user 2 tweets: [t2, B]
//     *
//     * feed = [A, B]
//     * after sort -> most recent first
//     *
//     * ⚠️ Edge Case:
//     * - If total tweets < 10 → avoid index out of bounds
//     *
//     * Complexity:
//     * -----------
//     * - Time: O(N log N) (sorting all collected tweets)
//     * - Space: O(N)
//     */
//    public List<Integer> getNewsFeed(int userId){
//        List<int[]> feed = new ArrayList<>(tweetMap.getOrDefault(userId, new ArrayList<>()));
//
//        for(int followeeId : followMap.getOrDefault(userId, new HashSet<>()))
//            feed.addAll(tweetMap.getOrDefault(followeeId, new ArrayList<>()));
//
//        feed.sort((a,b) -> b[0] - a[0]);
//
//        List<Integer> res = new ArrayList<>();
//        for(int i=0; i<10; i++)
//            res.add(feed.get(i)[1]);
//
//        return res;
//    }
//
//
//    /*
//     * follow(followerId, followeeId)
//     * ------------------------------
//     * Intuition:
//     * - Maintain a set of followees per user
//     *
//     * Steps:
//     * 1. Prevent self-follow
//     * 2. Add followee to follower's set
//     *
//     * Complexity:
//     * - Time: O(1)
//     * - Space: O(1)
//     */
//    public void follow(int followerId, int followeeId){
//        if(followerId != followeeId){
//            followMap.putIfAbsent(followerId, new HashSet<>());
//            followMap.get(followerId).add(followeeId);
//        }
//    }
//
//
//    /*
//     * unfollow(followerId, followeeId)
//     * --------------------------------
//     * Intuition:
//     * - Remove followee from follower's set
//     *
//     * Steps:
//     * 1. If follower exists → remove followee
//     *
//     * Complexity:
//     * - Time: O(1)
//     * - Space: O(1)
//     */
//    public void unfollow(int followerId, int followeeId){
//        followMap.getOrDefault(followerId, new HashSet<>())
//                .remove(followeeId);
//    }
//
//}

/*
 * 🚫 Why This is Brute Force (Limitations & Issues)
 * ------------------------------------------------
 * Although this approach works correctly, it is NOT scalable.
 *
 * Problem 1: Collecting All Tweets (Inefficient Merge)
 * ---------------------------------------------------
 * - We gather tweets from:
 *   -> the user
 *   -> ALL followees
 *
 * - If a user follows many people, and each has many tweets:
 *   -> Total tweets (N) becomes very large
 *
 * Example:
 * - User follows 1000 people
 * - Each has 1000 tweets
 * -> N = 1,000,000 tweets
 *
 * We unnecessarily process ALL of them just to return TOP 10.
 *
 *
 * Problem 2: Full Sorting (Expensive)
 * ----------------------------------
 * - We sort the entire list of tweets:
 *
 *      O(N log N)
 *
 * - But we only need the TOP 10 most recent tweets.
 *
 * 👉 This is overkill and wasteful.
 *
 *
 * Problem 3: Poor Scalability
 * --------------------------
 * - As number of users / tweets grows:
 *   - Memory usage increases (O(N))
 *   - Sorting cost becomes dominant
 *
 * - This approach will TLE (Time Limit Exceeded) in large inputs.
 *
 *
 * Problem 4: Not Using Sorted Nature
 * ---------------------------------
 * - Each user's tweets are already in chronological order.
 *
 * - But we IGNORE that and:
 *   -> dump everything together
 *   -> sort again from scratch
 *
 * 👉 This wastes useful structure.
 *
 *
 * ✅ Why We Need Heap (Optimized Approach)
 * ---------------------------------------
 * Instead of sorting everything:
 *
 * Use a MAX HEAP (PriorityQueue) to:
 * - Efficiently merge k sorted lists (like merge k sorted arrays)
 *
 *
 * Key Idea:
 * ---------
 * - Each user’s tweet list is already sorted by time.
 * - We only need the most recent tweets across all users.
 *
 * So:
 * 1. Push latest tweet of each user into heap
 * 2. Extract max (most recent)
 * 3. Push next tweet from that user
 * 4. Repeat until we get 10 tweets
 *
 *
 * Complexity Comparison:
 * ---------------------
 * Brute Force:
 *   Time:  O(N log N)
 *   Space: O(N)
 *
 * Heap Optimized:
 *   Time:  O(N log K)  (K = number of users followed)
 *   Space: O(K)
 *
 *
 * 💡 Intuition Shift:
 * ------------------
 * Instead of:
 *   "Sort everything"
 *
 * Think:
 *   "Only track what I actually need (top 10)"
 *
 *
 * This is why Heap approach is preferred.
 */


// -------------------------------------------------------------  //


// OPTIMIZED APPROACH:
public class DesignTwitter {

    /*
     * ============================================================================
     * DESIGN TWITTER: OPTIMIZED SYSTEM ARCHITECTURE
     * ============================================================================
     *
     * 1. CORE DATA STRUCTURES:
     * ------------------------
     * - userMap (HashMap): Maps a unique userId to a 'User' object.
     *   Enables O(1) retrieval of any user's profile and relationship data.
     *
     * - Tweet (Singly Linked List Node):
     *   Each tweet acts as a node in a linked list. Instead of an ArrayList,
     *   we use a Linked List to represent a user's timeline. The 'head' is
     *   always the LATEST tweet. This allows O(1) tweet insertion at the front.
     *
     * - User (Object):
     *   Encapsulates the user's personality: their unique ID, a HashSet of
     *   followed userIds (O(1) lookups), and a pointer to the head of their
     *   Tweet linked list.
     *
     * 2. ALGORITHMIC STRATEGY: "MERGE K-SORTED LISTS"
     * -----------------------------------------------
     * The most expensive operation is 'getNewsFeed'. A user can follow
     * hundreds of people, each with thousands of tweets.
     *
     * BRUTE FORCE: Collect all tweets from all followed users and sort them.
     * Complexity: O(N log N) where N = total tweets. (Very slow).
     *
     * OPTIMIZED (The Max Heap Approach):
     * Each user's linked list is already sorted by time (head is newest).
     * We only need the top 10 most recent tweets across ALL followed users.
     *
     * Steps:
     * 1. Initialize a Max Heap (PriorityQueue) with the 'tweetHead' of
     *    every user being followed. (Initial size = K followees).
     * 2. Poll the heap for the absolute latest tweet.
     * 3. Take that tweet, and push the NEXT (older) tweet from that same
     *    user's timeline into the heap.
     * 4. Repeat until 10 tweets are collected or the heap is empty.
     *
     * Complexity: O(K + 10 log K) where K is the number of followees.
     * This is incredibly efficient because we never look at the 11th tweet
     * of any user unless it's actually in the top 10 overall.
     *
     * 3. KEY CONSTRAINTS & EDGE CASES:
     * --------------------------------
     * - Self-Follow: A user must see their own tweets. We handle this by
     *   making every user follow themselves upon creation.
     * - Unfollow Safety: A user cannot unfollow themselves.
     * - Global Timestamp: A static integer 'timestamp' ensures a strict
     *   chronological order across the entire system.
     * ============================================================================
     */

    private static int timestamp = 0;
    private HashMap<Integer, User> userMap;

     /*
        *Tweet as a Linked List node.
        *head.next points towards to an older Tweet.
     */
    private class Tweet {
        int id;
        int time;
        Tweet next;

        public Tweet(int id){
            this.id = id;
            this.time = timestamp;
            this.next = null;
        }
    }

     /*
        *User tracks their own tweets and their follow set.
     */
    private class User {
        int id;
        Set<Integer> followed;
        Tweet tweetHead;

        public User(int id){
            this.id = id;
            followed = new HashSet<>();
            follow(id);
            tweetHead = null;
        }

        public void follow(int userId){
            followed.add(userId);
        }

        public void unfollow(int userId){
            if(this.id != userId)
                followed.remove(userId);
        }

        public void post(int tweetId){
            Tweet t = new Tweet(tweetId);
            t.next = tweetHead;
            tweetHead = t;
        }
    }

    public DesignTwitter(){
        userMap = new HashMap<>();
    }

    public void postTweet(int userId, int tweetId){
        userMap.putIfAbsent(userId, new User(userId));
        userMap.get(userId).post(tweetId);
    }

    public List<Integer> getNewsFeed(int userId){
        List<Integer> res = new ArrayList<>();

        if(!userMap.containsKey(userId))
            return res;

        PriorityQueue<Tweet> maxHeap = new PriorityQueue<>((a, b) -> b.time - a.time);

        Set<Integer> followedUsers = userMap.get(userId).followed;

        for(int followeeId : followedUsers){
            User followee = userMap.get(followeeId);
            if(followee != null && followee.tweetHead != null)
                maxHeap.offer(followee.tweetHead);
        }

        while(!maxHeap.isEmpty() && res.size() < 10){
            Tweet top = maxHeap.poll();
            res.add(top.id);

            if(top.next != null)
                maxHeap.offer(top.next);
        }

        return res;
    }

    public void follow(int followerId, int followeeId){
        userMap.putIfAbsent(followerId, new User(followerId));
        userMap.putIfAbsent(followeeId, new User(followeeId));
        userMap.get(followerId).follow(followeeId);
    }

    public void unfollow(int followerId, int followeeId){
        if(userMap.containsKey(followerId))
            userMap.get(followerId).unfollow(followeeId);
    }

}


