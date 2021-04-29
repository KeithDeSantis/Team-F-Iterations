package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.*;

public class BFSGraph implements IPathfindingAlgos {
    /**
     * Performs Depth-First Search on adjacency matrix
     * @param a vertex to start from
     * @return ArrayList of resultant path
     */
    public Path getPath(Graph graph, NodeEntry a, NodeEntry b) {
        HashMap<NodeEntry, NodeEntry> linkages = new HashMap<>();
        HashMap<NodeEntry, Double> weights = new HashMap<>();
        DoublyLinkedHashSet<NodeEntry> exhaustedVertices = new DoublyLinkedHashSet<>();
        DoublyLinkedHashSet<NodeEntry> nextChecked = new DoublyLinkedHashSet<>();
        nextChecked.add(a);
        weights.put(a, 0.0);
        boolean continueSearch = ! a.equals(b);
        while(continueSearch && nextChecked.size() > 0) {
            NodeEntry cursor = nextChecked.removeIndex(0);
            for(Edge neighborEdge : cursor.getEdges()) {
                NodeEntry neighbor = cursor.getNeighbor(neighborEdge);
                if(! exhaustedVertices.containsKey(neighbor)) {
                    nextChecked.add(neighbor);
                    linkages.put(neighbor, cursor);
                    double currentWeight = weights.get(neighbor);
                    if(neighbor.equals(b)) {
                        continueSearch = false;
                    }
                }
            }
            exhaustedVertices.add(cursor);
        }
        Path path = null;
        if(linkages.containsKey(b)) {
            path = new Path();
            path.addVertexToPath(b, 0);
            NodeEntry prev = linkages.get(b);
            while(prev != null) {
                path.insertVertexInPath(0, prev, 0);
                prev = linkages.get(prev);
            }
        }
        return path;
    }
}
