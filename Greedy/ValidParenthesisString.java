// https://leetcode.com/problems/valid-parenthesis-string/description/
import java.util.*;

class ValidParenthesisString
{
    public static void main(String[] args) 
    {
        String s = "(*)";

        System.out.println(validParenthesisStringBrute(s));

        System.out.println(validParenthesisStringBrute2(s));

        System.out.println(validParenthesisStringBetter(s));

        System.out.println(validParenthesisStringOptimized(s));



    }

    private static boolean validParenthesisStringBrute(String s) {
        return dfs(0,0,s);    
    }
    private static boolean dfs(int index, int open, String s) {
        if(open < 0)
            return false;

        if(index == s.length())
            return open == 0;
        
        char ch = s.charAt(index);
        if(ch == '(')
            return dfs(index+1, open+1, s);
        else if(ch == ')')
            return dfs(index+1, open-1, s);
        else
        {
            return dfs(index+1, open, s) || dfs(index+1, open+1, s) || dfs(index+1, open-1, s);
        }        
    }

    private static boolean validParenthesisStringBrute2(String s) {
        Boolean[][] dp = new Boolean[s.length()+1][s.length()+1];
        return dfs2(0,0,s,dp);
    }
    private static boolean dfs2(int index, int open, String s, Boolean[][] dp) {
        if(open < 0)
            return false;
        if(index == s.length())
            return open == 0;

        if(dp[index][open] != null)
            return dp[index][open];    

        boolean res;

        char ch = s.charAt(index);
        if(ch == '(')
            res = dfs2(index+1, open+1, s, dp);   
        else if(ch == ')')
            res = dfs2(index+1, open-1, s, dp); 
        else
        {
            res = dfs2(index+1, open, s, dp) || dfs2(index+1, open+1, s, dp) || dfs2(index+1, open-1, s, dp);          
        }

        dp[index][open] = res;
        return res;
    }

    private static boolean validParenthesisStringBetter(String s) {
        Stack<Integer> leftStack = new Stack<>();
        Stack<Integer> starStack = new Stack<>();

        for(int i=0; i<s.length(); i++)
        {
            char ch = s.charAt(i);
            
            if(ch == '(')
                leftStack.push(i);
            else if(ch == '*')
                starStack.push(i);
            else if(ch == ')')
            {
                if(!leftStack.isEmpty())
                    leftStack.pop();
                else if(!starStack.isEmpty())
                    starStack.pop();
                else 
                    return false;        
            }        
        }

        while(!leftStack.isEmpty() && !starStack.isEmpty())
        {
            int leftIndex = leftStack.pop();
            int starIndex = starStack.pop();

            if(leftIndex > starIndex)
                return false; 
        }

        return leftStack.isEmpty();
    }

    private static boolean validParenthesisStringOptimized(String s) {
        int low = 0;
        int high = 0;

        for(char c : s.toCharArray())
        {
            if(c == '(')
            {
                low++;
                high++;
            }
            else if(c == ')')
            {
                low--;
                high--;
            }
            else if(c == '*')
            {
                low--;
                high++;
            }


            if(high < 0)
                return false;
            if(low < 0)
                low = 0;     
        }

        return low == 0;
    }
}