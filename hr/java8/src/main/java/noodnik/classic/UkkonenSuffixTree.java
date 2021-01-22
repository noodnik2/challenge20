package noodnik.classic;

import static java.lang.String.format;
import static noodnik.lib.Common.invokeSupplierLogElapsedTime;
import static noodnik.lib.Common.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * see:
 * - https://www.cs.helsinki.fi/u/ukkonen/SuffixT1withFigs.pdf
 * - https://github.com/mutux/Ukkonen-s-Suffix-Tree-Algorithm
 * - https://github.com/mission-peace/interview/blob/master/src/com/interview/suffixprefix/SuffixTree.java
 * - https://gist.github.com/makagonov/22ab3675e3fc0031314e8535ffcbee2c
 * - https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
 * - https://brenden.github.io/ukkonen-animation/
 * - https://github.com/baratgabor/SuffixTree
 */
public class UkkonenSuffixTree {
    
    final String uchars;
    final Node uroot;
    final Consumer<String> consoleSinkFn;
    
    public UkkonenSuffixTree(String chars, Consumer<String> consoleSinkFn) {
        this.consoleSinkFn = consoleSinkFn;
        this.uchars = chars;
        uroot = invokeSupplierLogElapsedTime("build", () -> build(chars));
        log("nodes(%s)", Node.nextId);
    }
    
//    Node subTree(String chars) {
//        Node newRoot = uroot;
////      Node newRoot = new Node(uroot);
//        Collection<Node> nodesToKeep = new ArrayList<>();
//        for (int i = 0; i < chars.length(); i++) {
//            String subs = chars.substring(i);
//            nodesToKeep.addAll(findNodes(newRoot, subs));
//        }
//        Queue<Node> nodeStack = new LinkedList<>();
//        nodeStack.add(newRoot);
//        while(!nodeStack.isEmpty()) {
//            Node node = nodeStack.poll();
//            for (Map.Entry<Character, Edge> edgeEntry : node.outEdges.entrySet()) {
//                if (nodesToKeep.contains(edgeEntry.getValue().bnode)) {
//                    node.outEdges.remove(edgeEntry.getKey());
//                }
//            }
//        }
//        return newRoot;
//    }
//    
//    private String substr(String chars, int start, int end) {
//        return chars.substring(start, end == END_IND ? chars.length() : end);
//    }
//        
//    private List<Node> findNodes(Node root, String subs) {
//        int index = 0;
//        char edgeChar = subs.charAt(index);
//        Edge edge = root.getOutEdge(edgeChar);
//        if (edge == null) {
//            throw new RuntimeException("out edge not found for: " + edgeChar);
//        }
//        String edgeLabel = substr(uchars, edge.startIndex, edge.endIndex);
//        // cases to support:
//        //  1. edgeLabel longer than subs
//        //  2. edgeLabel equal length as subs
//        //  3. edgeLabel shorter than subs
//        if (edgeLabel.length() == subs.length()) {
//            //  2. edgeLabel equal length as subs
//            edge.bnode
//        }
//        return null;
//    }

    public UkkonenSuffixTree(String chars) {
        this(chars, null);
    }

    // TODO implement this for only [left..right] of the built tree
    public int calcSum(int left, int right) {
        log("calcSum[%s, %s]", left, right);
        return walkTreeNr(null, left, right);
    }
    
    public int calcSum() {        
        return walkTreeNr(null, 0, uchars.length() - 1);
    }    

    public void draw() {
        walkTreeNr(
            (edge, s, level) -> printBranch(edge, s, level),
            0,
            uchars.length() - 1
        );
    }

    private void printBranch(Edge edge, String s, int lvl) {
        final int maxLen = uchars.length() + 5;
        final String lp = lvl == 0 ? " " : "|";
        final String ep = lvl == 0 ? "+" : "|";
        final String linkId = edge.bnode.suffixLink != null ? "->" + edge.bnode.suffixLink : "";
        println(format("%s|", repeat(lp, maxLen * lvl)));
        println(format("%s| %s (%s)", repeat(lp, maxLen * lvl), s, s.length())); 
        println(format("%s%s-%s id:%s", ep, repeat(" ", maxLen * lvl), repeat("-", maxLen - 1), edge.bnode.id + linkId));
    }    

    public interface EdgeVisitor {
        void visit(Edge edge, String substr, int level);
    }

    public int walkTreeNr(final EdgeVisitor edgeVisitor, int left, int right) {
        return invokeSupplierLogElapsedTime("walkTreeNr", () -> walkTreeNr0(edgeVisitor, left, right));
    }

    private int walkTreeNr0(final EdgeVisitor edgeVisitor, int left, int right) {

        if (uroot.outEdges == null) {
            return 0;
        }
        
        final int charsLen = uchars.length();
        int cz = 0;

        // TODO implement left, right
        assert left == 0;
        assert right == charsLen - 1;

        Queue<NodeAtLevel> stack = new LinkedList<>();
        stack.add(new NodeAtLevel(uroot, 0));

        while(!stack.isEmpty()) {
            
            final NodeAtLevel nodeAtLevel = stack.poll();
            
            for (final Map.Entry<Character, Edge> edg : nodeAtLevel.node.outEdges.entrySet()) {
                
                Edge edge = edg.getValue();
                
                final int start = edge.startIndex;
                final int end = edge.endIndex != END_IND ? edge.endIndex : charsLen - 1;            

//                String considering = uchars.substring(start, end + 1);
//                String lookingFor = uchars.substring(left, right + 1);
//                log(
//                    "%s ? %s: %s", 
//                    considering, 
//                    lookingFor,
//                    lookingFor.contains(considering)
//                );
//                if (lookingFor.contains(considering)) {
                    cz += end - start + 1;
//                }
                
                if (edgeVisitor != null) {
//                    String substring = considering;
                    String substring = uchars.substring(start, end + 1);
                    edgeVisitor.visit(edge, substring, nodeAtLevel.level);
                }
    
              final Node node = edge.bnode;
                if (node.outEdges != null) {
                    stack.add(new NodeAtLevel(node, nodeAtLevel.level + 1));
                }
                
            }
            
        }
        
        return cz;
    }
    
    private Collection<Edge> find(int left, int right) {
        final List<Edge> edgeList = new ArrayList<>();
        int nextIndex = left;
        int foundLength = 0;
        int targetLength = right - left + 1;
        while(foundLength < targetLength) {
            char edgeChar = uchars.charAt(nextIndex);
            Edge edge = uroot.getOutEdge(edgeChar);
            if (edge == null) {
                throw new RuntimeException("edge not found for: " + edgeChar);
            }
            edgeList.add(edge);
            int edgeEnd = edge.endIndex == END_IND ? uchars.length() : edge.endIndex;
            int edgeLength = edgeEnd - edge.startIndex + 1;
            foundLength += edgeLength;
        }
        return edgeList;
    }

    private int walkTreeNr1(final EdgeVisitor edgeVisitor, int left, int right) {

        if (uroot.outEdges == null) {
            return 0;
        }
        
        final int charsLen = uchars.length();
        int cz = 0;

        // TODO implement left, right
        assert left == 0;
        assert right == charsLen - 1;

        Queue<NodeAtLevel> stack = new LinkedList<>();
        stack.add(new NodeAtLevel(uroot, 0));

        while(!stack.isEmpty()) {
            
            final NodeAtLevel nodeAtLevel = stack.poll();
            
            for (final Map.Entry<Character, Edge> edg : nodeAtLevel.node.outEdges.entrySet()) {
                
                Edge edge = edg.getValue();
                
                final int start = edge.startIndex;
                final int end = edge.endIndex != END_IND ? edge.endIndex : charsLen - 1;            

//                String considering = uchars.substring(start, end + 1);
//                String lookingFor = uchars.substring(left, right + 1);
//                log(
//                    "%s ? %s: %s", 
//                    considering, 
//                    lookingFor,
//                    lookingFor.contains(considering)
//                );
//                if (lookingFor.contains(considering)) {
                    cz += end - start + 1;
//                }
                
                if (edgeVisitor != null) {
//                    String substring = considering;
                    String substring = uchars.substring(start, end + 1);
                    edgeVisitor.visit(edge, substring, nodeAtLevel.level);
                }
    
              final Node node = edge.bnode;
                if (node.outEdges != null) {
                    stack.add(new NodeAtLevel(node, nodeAtLevel.level + 1));
                }
                
            }
            
        }
        
        return cz;
    }
    
    private static class NodeAtLevel {
        NodeAtLevel(Node node, int level) {
            this.node = node;
            this.level = level;
        }
        final Node node;
        final int level;
    }

    private String repeat(final String string, int i) {
        StringBuffer sb = new StringBuffer();
        while(i-->0) {
            sb.append(string);
        }
        return sb.toString();
    }
    
    private void println(String s) {
        if (consoleSinkFn != null) {
            consoleSinkFn.accept(s);
        }
    }
    
    /**
     *  NOTE: translation from Python of: https://github.com/mutux/Ukkonen-s-Suffix-Tree-Algorithm
     */
        
    private static int END_IND = -1;    // '#'
    
    private static class ParentKey {
        
        private Node node;
//        char c;
        
//        public String toString() {
//            return format("pkey(%s)", node);
//        }
        
        ParentKey(Node node, char c) {
            this.node = node;
//            this.c = c;
        }
        
    }

    private static class Node {
        
        static int nextId = 0;
        
        private ParentKey parentKey;
        private Map<Character, Edge> outEdges;
        private Node suffixLink;
        final int id;
        
//        public String toString() {
//            return format("node %d->%s: %s", id, parentKey, outEdges);
//        }
        
        private Node() {
            id = nextId++;
        }
        
        private void setOutEdge(final Character key, final Edge edge) {
            if (outEdges == null) {
                outEdges = new HashMap<>();
            }
            outEdges.put(key, edge);
        }
        
        private Edge getOutEdge(final Character key) {
            if (outEdges == null) {
                return null;
            }
            return outEdges.get(key);
        }
        
        private void setParentKey(final ParentKey parentKey) {
            this.parentKey = parentKey;
        }              
        
    }
    
    private static class Edge {
        
//        Node anode;       
        private int startIndex;
        private int endIndex;
        private Node bnode;
        
//        public String toString() {
//            return format("edge(%d:%d)->%s", startIndex, endIndex, bnode == null ? null : bnode.id);
//        }
        
        private Edge(Node anode, int startIndex, int endIndex, Node bnode) {
//            this.anode = anode;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.bnode = bnode;
        }
        
    }
    
    private static class StdResult {
        
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
    
    private static class UnfoldResult extends StdResult {
        private UnfoldResult(int remainder, Node actNode, Character actKey, int actLen) {
            super(remainder, actNode, actKey, actLen);
        }
    }
    
    private static class HopResult {
        
        Node actNode;
        Character actKey;
        int actLen;
        int actLenRe;
        
        private HopResult(Node actNode, Character actKey, int actLen, int actLenRe) {
            this.actNode = actNode;
            this.actKey = actKey;
            this.actLen = actLen;
            this.actLenRe = actLenRe;
        }
    }

    private static class StepResult extends HopResult {

        private boolean isLost;
        
        private StepResult(boolean isLost, Node actNode, Character actKey, int actLen, int actLenRe) {
            super(actNode, actKey, actLen, actLenRe);
            this.isLost = isLost;
        }
        
    }
    
    private static Node build(final String chars) {
                
        final Node root = new Node();
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
    
    private static UnfoldResult unfold(
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
    
    private static StepResult step(
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
    
    private static HopResult hop(
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
