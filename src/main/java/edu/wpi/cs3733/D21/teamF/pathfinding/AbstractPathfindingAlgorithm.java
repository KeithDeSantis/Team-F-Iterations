package edu.wpi.cs3733.D21.teamF.pathfinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPathfindingAlgorithm implements IPathfindingAlgorithm{
    public final Path getPath(Graph graph, Vertex a, Vertex b){
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

            Vertex next = getNext(graph, b, weights, exhaustedVertices);

            if(next == null) {
                continueGraphSearchIteration = false;
            } else {
                proxy = next;
                if(proxy.equals(b)) {
                    continueGraphSearchIteration = false;
                }
            }
        }

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

    public abstract Vertex getNext(Graph graph, Vertex goal, HashMap<String, Double> weights, List<Vertex> exhaustedVertices);
}
