package edu.wpi.cs3733.D21.teamF.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamF.pathfinding.*;

import java.util.HashMap;
import java.util.List;

public class DijkstraImpl extends AbstractPathfindingAlgorithm {

    @Override
    public Vertex getNext(Graph graph, Vertex goal, HashMap<String, Double> weights, List<Vertex> exhaustedVertices){
        Vertex next = null;
        for(String key : weights.keySet()) {
            Vertex vertex = graph.getVertex(key);
            if((graph.doesNotContain(exhaustedVertices, vertex))) {
                if(next == null) {
                    next = vertex;
                } else if(weights.get(vertex.getID()) < weights.get(next.getID())) {
                    next = vertex;
                }
            }
        }
        return next;
    }

}
