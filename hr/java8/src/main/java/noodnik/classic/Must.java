package noodnik.classic;

import static noodnik.lib.Common.log;

import java.util.Map;

public class Must {    
    
    static int EI = -1;
    
    static class Edge {
        int start, end;
        Node node;
        Edge(int start) {
            this.start = start;
            this.end = EI;
            node = new Node();
        }
        
    }
    
    static class Node {
        Map<Character, Edge> edgeMap;
    }
        
    static class Builder {
        
        String chars;
        Node activeNode;
        Edge activeEdge;
        int remaining;
        int activeLength;
        int end;

        // "xyzxyzxyz$"
        //  012345678
        
        void phase(int i) {
            
            remaining++;
            end++;
            
            char phaseCharacter = chars.charAt(i);
            
            // Rule 3 extension:
            //  1. increment activeLength by 1
            //  2. activeEdge is the edge having a start index of 'chars'
            while(remaining > 0) {
                // TODO take "activeLength" into account
                // "activeLength" is the offset into 'chars' to start looking
                Edge edge = activeNode.edgeMap.get(phaseCharacter);
                if (edge == null) {
                    edge = new Edge(i);
                    activeNode.edgeMap.put(phaseCharacter, edge);
                    remaining--;
                } else {
                    activeLength++;
                    break;
                }
            }
            
        }
        
        Node build(String chars) {
            
            this.chars = chars;
            
            Node root = new Node();
            
            activeNode = root;
            end = -1;
            
            for (int i = 0; i < chars.length(); i++) {
                phase(i);
            }
            
            return root;
        }

    }
    
    
    public Must(String chars) {
        
        Node root = new Builder().build(chars);
        
        
        
    }
        
    public int calcSum(int left, int right) {
        log("calcSum[%s, %s]", left, right);
        return 0;
    }
    
}
