package edu.wpi.cs3733.D21.teamF.pathfinding;

public class Edge {
    private final NodeEntry a, b;
    private final double weight;

    /**
     * Creates a new Edge
     * @param a one endpoint Vertex
     * @param b the other endpoint Vertex
     * @author Tony Vuolo
     */
    public Edge(NodeEntry a, NodeEntry b) {
        this.a = a;
        this.b = b;
        this.weight = a.EuclideanDistance(b);
        //TODO: change heuristic function?

        //Add the edge to each vertex.
        a.addEdge(this);
        b.addEdge(this);
    }

    /**
     * Gets the Vertices this Edge spans
     * @return {this.a, this.b}
     * @author Tony Vuolo
     */
    public NodeEntry[] getVertices() {
        return new NodeEntry[]{this.a, this.b};
    }

    /**
     * Gets the weight of this Edge
     * @return this.weight
     * @author Tony Vuolo
     */
    public double getWeight() {
        return this.weight;
    }

    /**
     * Checks if a Vertex is an endpoint of this Edge
     * @param vertex the comparator Vertex
     * @return true if the Vertex is this.a or this.b, else false
     * @author Tony Vuolo
     */
    public boolean isEndpoint(NodeEntry vertex) {
        return this.a.getID().equals(vertex.getID()) || this.b.getID().equals(vertex.getID());
    }

    /**
     * Determines if two Edges are equal
     * @param edge the comparator Edge
     * @return true if this Edge and the comparator Edge share the same endpoints
     * @author Tony Vuolo
     */
    public boolean equals(Edge edge) {
        if(edge != null) {
            boolean checkEnds = (this.a.getID().equals(edge.a.getID())) && (this.b.getID().equals(edge.b.getID()));
            boolean checkEndsReverse = (this.a.getID().equals(edge.b.getID())) && (this.b.getID().equals(edge.a.getID()));
            return checkEnds || checkEndsReverse;
        }
        return false;
    }
}
