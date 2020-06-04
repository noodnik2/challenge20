package noodnik.leetcode;

import static java.lang.Math.max;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class MaxDepthBinaryTree {

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { 
            this.val = val; 
        }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    @Rule
    public TestName testName = new TestName();
    
    @Test
    public void nullTestCase() {
        assertCorrectResult(0, null);    
    }

    @Test
    public void oneLevelTestCase() {
        assertCorrectResult(1, new TreeNode(1));
    }

    @Test
    public void balancedTestCase() {
        assertCorrectResult(
            2,
            new TreeNode(
                1,
                new TreeNode(2),
                new TreeNode(3)
            )
        );
    }

    @Test
    public void unBalancedLeftTestCase() {
        assertCorrectResult(
            2,
            new TreeNode(
                1,
                new TreeNode(2),
                null
            )
        );
    }

    @Test
    public void unBalancedRightTestCase() {
        assertCorrectResult(
            2,
            new TreeNode(
                1,
                null,
                new TreeNode(2)
            )
        );
    }

    @Test
    public void deepTestCase() {
        assertCorrectResult(
            5,
            new TreeNode(
                1,
                new TreeNode(2),
                new TreeNode(
                    3, 
                    new TreeNode(
                        4,
                        new TreeNode(5),
                        new TreeNode(
                            6,
                            new TreeNode(7),
                            null
                        )
                    ), 
                    new TreeNode(8)
                )
            )
        );
    }

    @Test
    public void leetCodeTestCase() {
        assertCorrectResult(
            3,
            new TreeNode(
                3,
                new TreeNode(9),
                new TreeNode(
                    20, 
                    new TreeNode(15),
                    new TreeNode(7)
                )
            )
        );
    }

    class RecursiveSolution {

        public int maxDepth(TreeNode root) {
            if (root == null) {
                return 0;
            }
            return 1 + max(
                maxDepth(root.left), 
                maxDepth(root.right)
            );
        }
        
    }

    class IterativeSolution {
        
        class TreeInfo {
            TreeInfo(TreeNode node, int depth) {
                this.node = node;
                this.depth = depth;
            }
            TreeNode node;
            int depth;
        }
        
        Queue<TreeInfo> stack = new LinkedList<>();

        public int maxDepth(TreeNode root) {
            
            if (root == null) {
                return 0;
            }
            
            stack.add(new TreeInfo(root, 1));
            
            int maxDepth = 1;
            while(stack.size() != 0) {
                TreeInfo head = stack.poll();
                if (head.node == null) {
                    continue;
                }
                if (maxDepth < head.depth) {
                    maxDepth = head.depth;
                }
                stack.add(new TreeInfo(head.node.left, head.depth + 1));
                stack.add(new TreeInfo(head.node.right, head.depth + 1));
            }
            
            return maxDepth;
        }
        
    }

    class Diagnostic {

        int depthCounter = 0;
        
        void walkTree(TreeNode node) {
            if (node == null) {
                return;
            }
            depthCounter++;
            log("- depth(%s) val(%s)", depthCounter, node.val);
            walkTree(node.left);
            walkTree(node.right);
            depthCounter--;
        }
        
    }
    
    void assertCorrectResult(int expectedResult, TreeNode node) {
        log("%s:", testName.getMethodName());
        new Diagnostic().walkTree(node);
        assertEquals(expectedResult, new IterativeSolution().maxDepth(node));
        assertEquals(expectedResult, new RecursiveSolution().maxDepth(node));
    }

}
