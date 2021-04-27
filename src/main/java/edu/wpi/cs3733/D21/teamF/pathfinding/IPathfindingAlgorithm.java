package edu.wpi.cs3733.D21.teamF.pathfinding;

public interface IPathfindingAlgorithm {
    Path getPath(Graph graph, Vertex a, Vertex b);
}
