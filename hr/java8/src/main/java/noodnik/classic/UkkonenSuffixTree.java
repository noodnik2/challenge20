package noodnik.classic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * see:
 * - https://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf
 * - https://github.com/mutux/Ukkonen-s-Suffix-Tree-Algorithm
 * - https://github.com/mission-peace/interview/blob/master/src/com/interview/suffixprefix/SuffixTree.java
 * - https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 */
public class UkkonenSuffixTree {
    
    final Consumer<String> consoleSinkFn;
    
    public UkkonenSuffixTree(Consumer<String> consoleSinkFn) {
        this.consoleSinkFn = consoleSinkFn;
    }
    
    public UkkonenSuffixTree() {
        this(null);
    }
    
    public int calcSum(final String chars) {        
        return walkTreeNr(build(chars), chars, 0, END_IND, null);
    }    

    public void draw(final String chars) {
        walkTree(
            build(chars), 
            chars, 
            0, 
            END_IND,
            (node, substr, v, charsLen) -> {                
                int maxLen = charsLen + 6;
                String linkId = "";
                if (node.suffixLink != null) {
                    linkId = "->" + node.suffixLink.id;
                }
                if (v == 0) {
                    println(repeat(" ", maxLen * v) + "|");
                    println(repeat(" ", maxLen * v) + "|" + repeat(" ", 3) + substr + " " + substr.length()); 
                    println("+" + repeat(" ", maxLen * v) + "-" + repeat("-", maxLen - 1) + "* (" + node.id + linkId + ")"); 
                } else {
                    println("|" + repeat(" ", maxLen * v) + "|");
                    println("|" + repeat(" ", maxLen * v) + "|" + repeat(" ", 3) + substr + " " + substr.length()); 
                    println("|" + repeat(" ", maxLen * v) + "+" + repeat("-", maxLen - 1) + "* (" + node.id + linkId + ")"); 
                }                
            }
        );
    }    

    int walkTree(
        final Node rnode, 
        final String chars, 
        final int v, 
        final int ed, 
        final NodeVisitor nodeVisitor
    ) {
        
        if (rnode.outEdges == null) {
            return 0;
        }
        
        int charsLen = chars.length();
        int cz = 0;

        for (final Map.Entry<Character, Edge> edg : rnode.outEdges.entrySet()) {
            
            int s = edg.getValue().startIndex;
            int t = edg.getValue().endIndex;            
            if (t == END_IND) {
                t = (ed == END_IND) ? charsLen : ed;
            }

            String substr = substr(chars, s, t + 1);
            int f = substr.length();

            Node node = edg.getValue().bnode;
            if (nodeVisitor != null) {
                nodeVisitor.visitNode(node, substr, v, charsLen);
            }

            cz += f;
            if (node.outEdges != null) {
                cz += walkTree(node, chars, v + 1, ed, nodeVisitor);
            }
            
        }
        
        return cz;
    }
    
    static class NodeAtLevel {
        NodeAtLevel(Node node, int level) {
            this.node = node;
            this.level = level;
        }
        Node node;
        int level;
    }

    int walkTreeNr(
        final Node rnode, 
        final String chars, 
        final int v,    // TODO remove - always 0
        final int ed,   // TODO remove - always END_IND
        final NodeVisitor nodeVisitor   // TODO remove??
    ) {
        
        if (rnode.outEdges == null) {
            return 0;
        }
        
        int charsLen = chars.length();
        int cz = 0;
        
        Queue<NodeAtLevel> stack = new LinkedList<>();
        stack.add(new NodeAtLevel(rnode, v));

        while(!stack.isEmpty()) {
            
            NodeAtLevel nodeAtLevel = stack.poll();
            
            for (final Map.Entry<Character, Edge> edg : nodeAtLevel.node.outEdges.entrySet()) {
                
                int s = edg.getValue().startIndex;
                int t = edg.getValue().endIndex;            
                if (t == END_IND) {
                    t = (ed == END_IND) ? charsLen : ed;
                }
    
                String substr = substr(chars, s, t + 1);
                int f = substr.length();
    
                Node node = edg.getValue().bnode;
                if (nodeVisitor != null) {
                    nodeVisitor.visitNode(node, substr, v, charsLen);
                }
    
                cz += f;
                if (node.outEdges != null) {
                    stack.add(new NodeAtLevel(node, v + 1));
//                    cz += walkTree(node, chars, v + 1, ed, nodeVisitor);
                }
                
            }
            
        }
        
        return cz;
    }

    interface NodeVisitor {
        void visitNode(Node node, String substr, int v, int charsLen);
    }

    String repeat(final String string, int i) {
        StringBuffer sb = new StringBuffer();
        while(i-->0) {
            sb.append(string);
        }
        return sb.toString();
    }
    
    String substr(final String s, final int start, int end) {
        int l = s.length();
        if (start < 0 || start >= l) {
            return "";
        }
        if (end <= start) {
            return "";
        }
        if (end >= l) {
            end = l;
        }
        return s.substring(start, end);
    }
    
    void println(String s) {
        if (consoleSinkFn != null) {
            consoleSinkFn.accept(s);
        }
    }
    
    /**
     *  NOTE: translation from Python of: https://github.com/mutux/Ukkonen-s-Suffix-Tree-Algorithm
     */
        
    static int END_IND = -1;    // '#'
    
    static class ParentKey {
        
        Node node;
        char c;
        
        ParentKey(Node node, char c) {
            this.node = node;
            this.c = c;
        }
        
    }

    static class Edge {
        
        Node anode;       
        int startIndex;
        int endIndex;
        Node bnode;
        
        Edge(Node anode, int startIndex, int endIndex, Node bnode) {
            this.anode = anode;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.bnode = bnode;
        }
        
    }
    
    static class StdResult {
        
        int remainder;
        Node actNode;
        Character actKey;
        int actLen;
        
        StdResult(int remainder, Node actNode, Character actKey, int actLen) {
            this.remainder = remainder;
            this.actNode = actNode;
            this.actKey = actKey;
            this.actLen = actLen;
        }
        
    }
    
    static class UnfoldResult extends StdResult {
        UnfoldResult(int remainder, Node actNode, Character actKey, int actLen) {
            super(remainder, actNode, actKey, actLen);
        }
    }
    
    static class HopResult {
        
        Node actNode;
        Character actKey;
        int actLen;
        int actLenRe;
        
        HopResult(Node actNode, Character actKey, int actLen, int actLenRe) {
            this.actNode = actNode;
            this.actKey = actKey;
            this.actLen = actLen;
            this.actLenRe = actLenRe;
        }
    }

    static class StepResult extends HopResult {

        boolean isLost;
        
        StepResult(boolean isLost, Node actNode, Character actKey, int actLen, int actLenRe) {
            super(actNode, actKey, actLen, actLenRe);
            this.isLost = isLost;
        }
        
    }
    
    public static class Node {
        
        static int nextId = 0;
        
        ParentKey parentKey;
        Map<Character, Edge> outEdges;
        Node suffixLink;
        final int id;
        
        Node() {
            id = nextId++;
        }
        
        void setOutEdge(final Character key, final Edge edge) {
            if (outEdges == null) {
                outEdges = new HashMap<>();
            }
            outEdges.put(key, edge);
        }
        
        Edge getOutEdge(final Character key) {
            if (outEdges == null) {
                return null;
            }
            return outEdges.get(key);
        }
        
        void setParentKey(final ParentKey parentKey) {
            this.parentKey = parentKey;
        }
        
    }
    
    public Node build(final String chars) {
                
        Node root = new Node();
        Node actNode = root;
        Character actKey = null;
        int actLen = 0;
        int remainder = 0;
        int ind = 0;
        
        for (int i = 0; i < chars.length(); i++) {
            char ch = chars.charAt(i);
            if (remainder == 0) {
                if (actNode.outEdges != null && actNode.outEdges.containsKey(ch)) {
                    actKey = ch;
                    actLen = 1;
                    remainder = 1;
                    Edge outEdge = actNode.getOutEdge(actKey);
                    int end = outEdge.endIndex;
                    int start = outEdge.startIndex;
                    if (end == END_IND) {
                        end = ind;
                    }
                    if ((end - start + 1) == actLen) {
                        actNode = actNode.getOutEdge(actKey).bnode;
                        actKey = null;
                        actLen = 0;
                    }
                } else {
                    Node aleaf = new Node();
                    Edge aedge = new Edge(actNode, ind, END_IND, aleaf);
                    aleaf.setParentKey(new ParentKey(actNode, chars.charAt(ind)));
                    actNode.setOutEdge(chars.charAt(ind), aedge);
                }
            } else {
                if (actKey == null && actLen == 0) {
                    if (actNode.outEdges.containsKey(ch)) {
                        actKey = ch;
                        actLen = 1;
                        remainder++;
                    } else {
                        remainder++;
                        UnfoldResult unfoldResult = unfold(root, chars, ind, remainder, actNode, actKey, actLen);
                        remainder = unfoldResult.remainder;
                        actNode = unfoldResult.actNode;
                        actKey = unfoldResult.actKey;
                        actLen = unfoldResult.actLen;
                    }
                } else {
                    Edge outEdge = actNode.getOutEdge(actKey);
                    int end = outEdge.endIndex;
                    int start = outEdge.startIndex;
                    if (end == END_IND) {
                        end = ind;
                    }
                    int comparePosition = start + actLen;
                    if (chars.charAt(comparePosition) != ch) {
                        remainder++;
                        UnfoldResult unfoldResult = unfold(root, chars, ind, remainder, actNode, actKey, actLen);
                        remainder = unfoldResult.remainder;
                        actNode = unfoldResult.actNode;
                        actKey = unfoldResult.actKey;
                        actLen = unfoldResult.actLen;
                    } else {
                        if (comparePosition < end) {
                            actLen++;
                            remainder++;
                        } else {
                            remainder++;
                            actNode = actNode.getOutEdge(actKey).bnode;
                            if (comparePosition == end) {
                                actLen = 0;
                                actKey = null;
                            } else {
                                actLen = 1;
                                actKey = ch;                                
                            }
                        }
                    }
                }
            }
            ind++;
            
        }
 
        return root;
        
    }
    
    UnfoldResult unfold(
        final Node root, 
        final String chars, 
        final int ind, 
        int remainder, 
        Node actNode, 
        Character actKey, 
        int actLen
    ) {
        
        Node preNode = null;
        while(remainder > 0) {
            
            String remains = chars.substring(ind - remainder + 1, ind + 1);            
            int actLenRe = remains.length() - 1 - actLen;
            
            HopResult hopResult = hop(ind, actNode, actKey, actLen, remains, actLenRe);
            actNode = hopResult.actNode;
            actKey = hopResult.actKey;
            actLen = hopResult.actLen;
            actLenRe = hopResult.actLenRe;
            
            StepResult stepResult = step(chars, ind, actNode, actKey, actLen, remains, actLenRe);
            actNode = stepResult.actNode;
            actKey = stepResult.actKey;
            actLen = stepResult.actLen;
            actLenRe = stepResult.actLenRe;            
            
            if (stepResult.isLost) {
                if (actLen == 1 && preNode != null && actNode != root) {
                    preNode.suffixLink = actNode;
                }
                return new UnfoldResult(remainder, actNode, actKey, actLen);
            }
            
            Node aleaf = null;
            if (actLen == 0) {
                if (!actNode.outEdges.containsKey(remains.charAt(actLenRe))) {
                    aleaf = new Node();
                    Edge aedge = new Edge(actNode, ind, END_IND, aleaf);
                    aleaf.setParentKey(new ParentKey(actNode, chars.charAt(ind)));
                    actNode.setOutEdge(chars.charAt(ind), aedge);
                }
            } else {    // on edge
                Edge outEdge = actNode.getOutEdge(actKey);
                if (remains.charAt(actLenRe + actLen) != chars.charAt(outEdge.startIndex + actLen)) {
                    // split
                    Node newNode = new Node();
                    Edge newEdge1 = new Edge(actNode, outEdge.startIndex, outEdge.startIndex + actLen - 1, newNode);
                    Edge newEdge2 = new Edge(newNode, outEdge.startIndex + actLen, outEdge.endIndex, outEdge.bnode);
                    actNode.setOutEdge(actKey,  newEdge1);
                    newNode.setParentKey(new ParentKey(actNode,  actKey));
                    newNode.setOutEdge(chars.charAt(outEdge.startIndex + actLen), newEdge2);
                    aleaf = new Node();
                    Edge aedge = new Edge(newNode, ind, END_IND, aleaf);
                    aleaf.setParentKey(new ParentKey(newNode,  chars.charAt(ind)));
                    newNode.setOutEdge(chars.charAt(ind), aedge);
                } else {
                    return new UnfoldResult(remainder, actNode, actKey, actLen);
                }
            }
            if (preNode != null && aleaf != null && aleaf.parentKey.node != root) {
                preNode.suffixLink = aleaf.parentKey.node;
            }
            if (aleaf != null && aleaf.parentKey.node != root) {
                preNode = aleaf.parentKey.node;
            }
            if (actNode == root && remainder > 1) {
                actKey = remains.charAt(1);
                actLen--;
            }
            if (actNode.suffixLink != null) {
                actNode = actNode.suffixLink;
            } else {
                actNode = root;
            }
            remainder--;
        }
        return new UnfoldResult(remainder, actNode, actKey, actLen);
    }
    
    StepResult step(
        final String chars, 
        final int ind, 
        final Node actNode, 
        Character actKey, 
        int actLen, 
        final String remains, 
        final int indRemainder
    ) {
        String remLabel = remains.substring(indRemainder);
        if (actLen > 0) {
            Edge outEdge = actNode.getOutEdge(actKey);
            int start = outEdge.startIndex;
            int end = outEdge.endIndex;
            if (end == END_IND) {
                end = ind;
            }
            String edgeLabel = chars.substring(start, end + 1);
            if (edgeLabel.startsWith(remLabel)) {
                actLen = remLabel.length();
                actKey = remLabel.charAt(0);
                return new StepResult(true, actNode, actKey, actLen, indRemainder);
            }
        } else {
            if (indRemainder < remains.length() && actNode.outEdges.containsKey(remains.charAt(indRemainder))) {
                Edge outEdge = actNode.getOutEdge(remains.charAt(indRemainder));
                int start = outEdge.startIndex;
                int end = outEdge.endIndex;
                if (end == END_IND) {
                    end = ind;                   
                }
                String edgeLabel = chars.substring(start, end + 1);
                if (edgeLabel.startsWith(remLabel)) {
                    actLen = remLabel.length();
                    actKey = remLabel.charAt(0);
                    return new StepResult(true, actNode, actKey, actLen, indRemainder);
                }
            }
        }
        return new StepResult(false, actNode, actKey, actLen, indRemainder);
    }
    
    HopResult hop(
        final int ind, 
        Node actNode, 
        Character actKey, 
        int actLen, 
        final String remains, 
        int indRemainder
    ) {
        if (actLen == 0 || actKey == null) {
            return new HopResult(actNode, actKey, actLen, indRemainder);
        }
        Edge outEdge = actNode.getOutEdge(actKey);
        int start = outEdge.startIndex;
        int end = outEdge.endIndex;
        if (end == END_IND) {
            end = ind;
        }
        int edgeLength = end - start + 1;
        while(actLen > edgeLength) {
            actNode = actNode.getOutEdge(actKey).bnode;
            indRemainder += edgeLength;
            actKey = remains.charAt(indRemainder);
            actLen -= edgeLength;
            outEdge = actNode.getOutEdge(actKey);
            start = outEdge.startIndex;
            end = outEdge.endIndex;
            if (end == END_IND) {
                end = ind;              
            }
            edgeLength = end - start + 1;
        }
        if (actLen == edgeLength) {
            actNode = actNode.getOutEdge(actKey).bnode;
            actKey = null;
            actLen = 0;
            indRemainder += edgeLength;
        }
        return new HopResult(actNode, actKey, actLen, indRemainder);
    }
    
}
