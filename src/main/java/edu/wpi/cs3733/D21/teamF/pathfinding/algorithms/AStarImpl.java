package edu.wpi.cs3733.D21.teamF.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamF.pathfinding.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AStarImpl implements IPathfindingAlgorithm {


    @Override
    public Path getPath(Graph graph, NodeEntry a, NodeEntry b) {
        if (!graph.contains(a) || !graph.contains(b)) {
            return null;
        }
        List<NodeEntry> exhaustedVertices = new LinkedList<>();
        HashMap<String, Double> weights = new HashMap<>();
        HashMap<String, NodeEntry> linkages = new HashMap<>();
        NodeEntry proxy = a;
        weights.put(proxy.getNodeID(), 0.0);
        boolean continueGraphSearchIteration = !proxy.equals(b);
        while(continueGraphSearchIteration) {
            exhaustedVertices.add(proxy);
            for(EdgeEntry edge : proxy.getEdges()) {
                NodeEntry neighbor = proxy.getNeighbor(edge);
                if(graph.doesNotContain(exhaustedVertices, neighbor)) {
                    NodeEntry prev = linkages.get(neighbor.getNodeID());
                    Double dijkstraWeight = weights.get(proxy.getNodeID()) + edge.getWeight();
                    if(prev != null) {
                        if(weights.get(neighbor.getNodeID()) > dijkstraWeight) {
                            weights.replace(neighbor.getNodeID(), dijkstraWeight);
                            linkages.replace(neighbor.getNodeID(), proxy);
                        }
                    } else {
                        weights.put(neighbor.getNodeID(), dijkstraWeight);
                        linkages.put(neighbor.getNodeID(), proxy);
                    }
                }
            }
            NodeEntry next = null;
            for(String key : weights.keySet()) {
                NodeEntry vertex = graph.getVertex(key);
                if((graph.doesNotContain(exhaustedVertices, vertex))) {
                    if(next == null) {
                        next = vertex;
                    } else if(weights.get(vertex.getNodeID()) + vertex.heuristic(b) < weights.get(next.getNodeID()) + next.heuristic(b)) {
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
//                proxy = linkages.get(proxy.getNodeID());
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
//            path.addVertexToPath(b, weights.get(b.getNodeID()));
//        }
//        return path == null ? null : path.asList();

        Path path = null;
        if(proxy.equals(b)) {
            path = new Path();
            path.addVertexToPath(b, weights.get(b.getNodeID()));
            NodeEntry cursor = linkages.get(b.getNodeID());
            while(cursor != null) {
                path.insertVertexInPath(0, cursor, 0);
                cursor = linkages.get(cursor.getNodeID());
            }
        }
        return path;
    }
}
