package edu.wpi.cs3733.D21.teamF.pathfinding;

import edu.wpi.cs3733.D21.teamF.pathfinding.algorithms.*;

import java.util.*;

public class Graph {
    private final List<Edge> edges;
    private final HashMap<String, Vertex> vertices;

    private IPathfindingAlgorithm pathfindingAlgorithm;

    /**
     * Creates a new Graph
     * @author Tony Vuolo
     */
    public Graph() {
        this.edges = new LinkedList<>();
        this.vertices = new HashMap<>();

        //Default to A*
        this.pathfindingAlgorithm = new AStarImpl();
    }

    /**
     * Adds an Edge to this Graph
     * @param edge the additive Edge
     * @return true if both endpoint Vertices are already contained in the Graph, else false
     * @author Tony Vuolo
     */
    public boolean addEdge(Edge edge) {
        this.edges.add(edge);
        Vertex[] endpoints = edge.getVertices();
        return this.vertices.get(endpoints[0].getID()) != null && this.vertices.get(endpoints[1].getID()) != null;
    }

    /**
     * Adds a Vertex to this Graph
     * @param vertex the additive Vertex
     * @author Tony Vuolo
     */
    public void addVertex(Vertex vertex) {
        this.vertices.put(vertex.getID(), vertex);
    }

    /**
     * Determines whether this Graph contains a specific Edge
     * @param edge the comparator Edge
     * @return true if the comparator Edge is contained in this Graph, else false
     * @author Tony Vuolo
     */
    public boolean contains(Edge edge) {
        for(Edge query : this.edges) {
            if(query.equals(edge)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to get the list of vertices.
     * @return An array containing all of our vertices.
     * @author Alex Friedman (ahf), Tony Vuolo
     */
    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices.values());
    }

    /**
     * USed to get a vertex from its ID.
     * @param vertexID The ID of the vertex
     * @return The vertex
     * @author Alex Friedman (ahf), Tony Vuolo
     */
    public Vertex getVertex(String vertexID)
    {
        return this.vertices.get(vertexID);
    }

    /**
     * Determines whether this Graph contains a specific Vertex
     * @param vertex the comparator Vertex
     * @return true if the comparator Vertex is contained in this Graph, else false
     * @author Tony Vuolo
     */
    public boolean contains(Vertex vertex) {
        for(String key : this.vertices.keySet()) {
            Vertex query = this.vertices.get(key);
            if(query.equals(vertex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the path of least weight between two Vertices
     * @param a the start Vertex
     * @param b the endpoint Vertex
     * @return the List of Vertices spanning the path of least weight from Vertex a to Vertex b
     * @author Tony Vuolo, Alex Friedman (ahf)
     */
    public Path getPath(Vertex a, Vertex b) {
        return pathfindingAlgorithm.getPath(this, a, b);
    }

    /**
     * Gets the Path of least weight between two vertices
     * @param v the (ordered) List of Vertices to be reached
     * @return the Path of least weight that will travel to every Vertex in the List in order
     */
    public Path getPath(List<Vertex> v) {
        Path path = new Path();
        ListIterator<Vertex> iterator = v.listIterator();
        Vertex prev = iterator.next(), current;
        path.addVertexToPath(prev, 0);
        while(iterator.hasNext()) {
            current = iterator.next();
            path.concatenate(getPath(prev, current));
            prev = current;
        }
        return path;
    }

    /**
     * Used to change the pathfinding algorithm type.
     * @param algorithmName The name of the algorithm to use (AStar/A*, BFS, DFS);
     * @return true if we successfully changed the pathfinding algorithm. False if the specified algorithm could not be found.
     * @author Alex Friedman (ahf)
     */
    public boolean setPathfindingAlgorithm(String algorithmName)
    {
        switch (algorithmName.toLowerCase())
        {
            case "astar":
            case "a*":
            case "a star":
                this.pathfindingAlgorithm = new AStarImpl();
                return true;
            case "dfs":
            case "depth-first-search":
                this.pathfindingAlgorithm = new DFSImpl();
                return true;
            case "bfs":
            case "breadth-first-search":
                this.pathfindingAlgorithm = new BFSImpl();
                return true;
            case "bestfirst":
            case "best-first-search":
                this.pathfindingAlgorithm = new BestFSImpl();
                return true;
            case "dijkstra":
            case "dijkstra's search":
                this.pathfindingAlgorithm = new DijkstraImpl();
                return true;
        }
        return false;
    }


    /**
     * Determines if a given List contains a specific Vertex
     * @param list the given List
     * @param vertex the comparator Vertex
     * @return true if some value in the List is congruent to the Vertex, else false
     * @author Tony Vuolo
     */
    public boolean doesNotContain(List<Vertex> list, Vertex vertex) {
        for(Vertex query : list) {
            if(query.equals(vertex)) {
                return false;
            }
        }
        return true;
    }
}
