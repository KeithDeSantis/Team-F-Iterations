package edu.wpi.cs3733.D21.teamF.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamF.pathfinding.AbstractPathfindingAlgorithm;
import edu.wpi.cs3733.D21.teamF.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamF.pathfinding.Vertex;

import java.util.HashMap;
import java.util.List;

public class BestFSImpl extends AbstractPathfindingAlgorithm {

    @Override
    public Vertex getNext(Graph graph, Vertex goal, HashMap<String, Double> weights, List<Vertex> exhaustedVertices){
        Vertex next = null;
        for(String key : weights.keySet()) {
            Vertex vertex = graph.getVertex(key);
            if((graph.doesNotContain(exhaustedVertices, vertex))) {
                if(next == null) {
                    next = vertex;
                } else if(weights.get(vertex.getID()) + vertex.heuristic(goal) < weights.get(next.getID()) + next.heuristic(goal))
                    next = vertex;
                }
            }
        return next;
    }

}
