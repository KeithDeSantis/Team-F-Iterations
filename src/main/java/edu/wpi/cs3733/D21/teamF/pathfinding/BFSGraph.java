package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.*;

public class BFSGraph implements IPathfindingAlgos {
    /**
     * Performs Depth-First Search on adjacency matrix
     * @param a vertex to start from
     * @return ArrayList of resultant path
     */
    public Path getPath(Graph graph, Vertex a, Vertex b) {
        HashMap<Vertex, Vertex> linkages = new HashMap<>();
        HashMap<Vertex, Double> weights = new HashMap<>();
        DoublyLinkedHashSet<Vertex> exhaustedVertices = new DoublyLinkedHashSet<>();
        DoublyLinkedHashSet<Vertex> nextChecked = new DoublyLinkedHashSet<>();
        nextChecked.add(a);
        weights.put(a, 0.0);
        boolean continueSearch = ! a.equals(b);
        while(continueSearch && nextChecked.size() > 0) {
            Vertex cursor = nextChecked.removeIndex(0);
            for(Edge neighborEdge : cursor.getEdges()) {
                Vertex neighbor = cursor.getNeighbor(neighborEdge);
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
            Vertex prev = linkages.get(b);
            while(prev != null) {
                path.insertVertexInPath(0, prev, 0);
                prev = linkages.get(prev);
            }
        }
        return path;
    }
}
