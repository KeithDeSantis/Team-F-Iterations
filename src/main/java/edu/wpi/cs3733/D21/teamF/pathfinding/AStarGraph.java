package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.*;
import java.util.stream.Collectors;

public class AStarGraph implements IPathfindingAlgos {
    /**
     * Finds the path of least weight between two Vertices using an A* algorithm
     * @param a the start Vertex
     * @param b the endpoint Vertex
     * @return the List of Vertices spanning the path of least weight from Vertex a to Vertex b
     * @author Tony Vuolo
     */
    public Path getPath(Graph graph, Vertex a, Vertex b) {
        if (!graph.contains(a) || !graph.contains(b)) {
            return null;
        }
        List<Vertex> exhaustedVertices = new LinkedList<>();
        HashMap<String, Double> weights = new HashMap<>();
        HashMap<String, Vertex> linkages = new HashMap<>();
        Vertex proxy = a;
        weights.put(proxy.getID(), 0.0);
        boolean continueGraphSearchIteration = !proxy.equals(b);
        while(continueGraphSearchIteration) {
            exhaustedVertices.add(proxy);
            for(Edge edge : proxy.getEdges()) {
                Vertex neighbor = proxy.getNeighbor(edge);
                if(graph.doesNotContain(exhaustedVertices, neighbor)) {
                    Vertex prev = linkages.get(neighbor.getID());
                    Double dijkstraWeight = weights.get(proxy.getID()) + edge.getWeight();
                    if(prev != null) {
                        if(weights.get(neighbor.getID()) > dijkstraWeight) {
                            weights.replace(neighbor.getID(), dijkstraWeight);
                            linkages.replace(neighbor.getID(), proxy);
                        }
                    } else {
                        weights.put(neighbor.getID(), dijkstraWeight);
                        linkages.put(neighbor.getID(), proxy);
                    }
                }
            }
            Vertex next = null;
            for(String key : weights.keySet()) {
                Vertex vertex = graph.getVertex(key);
                if((graph.doesNotContain(exhaustedVertices, vertex))) {
                    if(next == null) {
                        next = vertex;
                    } else if(weights.get(vertex.getID()) + vertex.heuristic(b) < weights.get(next.getID()) + next.heuristic(b)) {
                        next = vertex;
                    }
                }
            }
            if(next == null) {
                continueGraphSearchIteration = false;
            } else {
                proxy = next;
                if(proxy.equals(b)) {
                    continueGraphSearchIteration = false;
                }
            }
        }
//        List<Vertex> pathList = null;
//        if(proxy.equals(b)) {
//            pathList = new LinkedList<>();
//            boolean continueBackwardPathGeneration = true;
//            while(continueBackwardPathGeneration) {
//                proxy = linkages.get(proxy.getID());
//                if (proxy == null) {
//                    continueBackwardPathGeneration = false;
//                } else {
//                    pathList.add(0, proxy);
//                }
//            }
//        }
//        Path path = new Path();
//        if(pathList == null) {
//            path = null;
//        } else {
//            for(Vertex vertex : pathList) {
//                path.addVertexToPath(vertex, 0);
//            }
//            path.addVertexToPath(b, weights.get(b.getID()));
//        }
//        return path == null ? null : path.asList();

        Path path = null;
        if(proxy.equals(b)) {
            path = new Path();
            path.addVertexToPath(b, weights.get(b.getID()));
            Vertex cursor = linkages.get(b.getID());
            while(cursor != null) {
                path.insertVertexInPath(0, cursor, 0);
                cursor = linkages.get(cursor.getID());
            }
        }
        return path;
    }

}
